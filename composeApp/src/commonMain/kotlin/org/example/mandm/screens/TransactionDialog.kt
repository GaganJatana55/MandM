package org.example.mandm.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDate
import kotlinx.datetime.toLocalDateTime
import mandm.composeapp.generated.resources.Res
import mandm.composeapp.generated.resources.add_user_icon
import org.example.mandm.DateTimeUtil
import org.example.mandm.PriceMode
import org.example.mandm.PricingUtils
import org.example.mandm.TransactionTypeConstants
import org.example.mandm.UserTypeConstants
import org.example.mandm.commonBorder
import org.example.mandm.commonComponent.AutoCompleteDropdown
import org.example.mandm.commonComponent.BuySellSwitch
import org.example.mandm.commonComponent.DateInputField
import org.example.mandm.commonComponent.FilledActionButton
import org.example.mandm.commonComponent.OutlineActionButton
import org.example.mandm.commonComponent.SelectableOptionWithCheckbox
import org.example.mandm.commonComponent.ValidatedInputField
import org.example.mandm.commonComponent.CustomerSearchField
import org.example.mandm.dataModel.CustomerEntity
import org.example.mandm.dataModel.CustomerRouteEntity
import org.example.mandm.dataModel.MilkTransactionEntity
import org.example.mandm.dataModel.MoneyTransactionEntity
import org.example.mandm.formatMoney
import org.example.mandm.roundCorner
import org.example.mandm.theme.AppColors
import org.koin.compose.koinInject
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import org.example.mandm.viewModels.DashboardViewModel
import org.example.mandm.viewModels.MilkTransactionDialogViewModel
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

sealed class Tabs {
    object MilkOnly : Tabs()
    object MoneyOnly : Tabs()
    object MilkAndMoney : Tabs()
}

@Preview
@Composable
fun TransactionDialog(
    onDismiss: () -> Unit = {},
    showTabs: Tabs = Tabs.MilkAndMoney,
    inRoute: Boolean = true,
    onNextCLick: () -> Unit = {},
    onPrevClick: () -> Unit = {},
    onSaveClick: () -> Unit = {},
    onSavedDetail: (CustomerRouteEntity?, MilkTransactionEntity?, MoneyTransactionEntity?) -> Unit = { _, _, _ -> },
    onSkipClick: () -> Unit = {},
    routeMap: CustomerRouteEntity? = null,
    existingMilkTx: MilkTransactionEntity? = null,
    existingMoneyTx: MoneyTransactionEntity? = null,
    isEditing: Boolean = false,
    initialCustomer: CustomerEntity? = null

) {
    Dialog(
        onDismissRequest = onDismiss, properties = DialogProperties(
            usePlatformDefaultWidth = false, dismissOnClickOutside = true, dismissOnBackPress = true
        )
    ) {
        val dashboardViewModel: DashboardViewModel = koinViewModel()
        val milkVm: MilkTransactionDialogViewModel = koinViewModel()
        val scope = rememberCoroutineScope()
        LaunchedEffect(routeMap, existingMilkTx, existingMoneyTx, initialCustomer) {
            milkVm.initWith(routeMap, existingMilkTx, existingMoneyTx, initialCustomer)
        }
        val milkUi = milkVm.ui.collectAsState()
        var selectedTab by rememberSaveable { mutableStateOf(0) }
        Row(
            modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.1f))
                .clickable(onClick = {
                    onDismiss.invoke()
                }),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
//            if (inRoute) {
//                Spacer(Modifier.width(4.dp))
//                RoundedSideIconButton(
//                    shape = RoundedCornerShape(topStart = 60.dp, bottomStart = 60.dp),
//                    iconPainter = painterResource(Res.drawable.left_icon),
//                    onClick = { onPrevClick() })
//
//            }
            Spacer(Modifier.width(2.dp))
            Surface(
                modifier = Modifier.padding(6.dp).weight(1f).clickable(false, onClick = {}),
                shape = roundCorner(),

            ) {
                Column {
                    var showCreateUser by rememberSaveable { mutableStateOf(false) }
                    TabRow(
                        selectedTabIndex = selectedTab,

                    ) {
                        if (showTabs == Tabs.MilkOnly || showTabs == Tabs.MilkAndMoney) {
                            Tab(selectedTab == 0, onClick = {
                                selectedTab = 0
                            }) {
                                TabText(text = "Milk")
                            }
                        }

                        if (showTabs == Tabs.MoneyOnly || showTabs == Tabs.MilkAndMoney) {
                            Tab(selectedTab == 0, onClick = {
                                selectedTab = 1
                            }) {
                                TabText(text = "Money")
                            }
                        }

                    }
                    Row(
                        modifier = Modifier.padding(top = 10.dp).padding(horizontal = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(Modifier.weight(1f)) {
                            CustomerSearchField(
                                query = milkUi.value.query,
                                results = milkUi.value.searchResults,
                                onQueryChange = { milkVm.updateQuery(it) },
                                onSelect = { milkVm.selectCustomer(it) },
                                onMoveFocusNext = { /* option: move focus to price/qty */ }
                            )
                        }
                        Image(
                            modifier = Modifier.size(42.dp)
                                .background(color = MaterialTheme.colorScheme.surface)
                                .commonBorder().padding(4.dp)
                                .clickable { showCreateUser = true },
                            painter = painterResource(Res.drawable.add_user_icon),
                            contentDescription = "Add user Button",
                            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
                        )

                    }
                    when (selectedTab) {
                        0 -> MilkTransaction(
                            Modifier.padding(top = 8.dp),
                            user = milkUi.value.selectedCustomer,
                            onSubmit = { draft ->
                                scope.launch {
                                    milkVm.saveOrUpdate(isEditing, draft, DateTimeUtil.currentUtcDateString())
                                    onSavedDetail(routeMap, existingMilkTx, null)
                                    if (!inRoute) onDismiss() else onSaveClick()
                                }
                            },
                            onSkipClick = onSkipClick,
                            existing = existingMilkTx,
                            isEditing = isEditing,
                            routeMap = routeMap
                        )
                        else -> MoneyTransaction(
                            Modifier.padding(top = 8.dp),
                            user = milkUi.value.selectedCustomer,
                            onSubmit = { draft ->
                                scope.launch {
                                    milkVm.saveOrUpdateMoney(isEditing, draft, DateTimeUtil.currentUtcDateString())
                                    onSavedDetail(routeMap, existingMilkTx, existingMoneyTx)
                                    if (!inRoute) onDismiss() else onSaveClick()
                                }
                            },
                            onSkipClick = onSkipClick,
                            existing = existingMoneyTx,
                            isEditing = isEditing
                        )
                    }
                    if (showCreateUser) {
                        CreateUserDialog(
                            onDismiss = { showCreateUser = false },
                            onSaved = {
                                milkVm.selectCustomer(it)
                                showCreateUser = false
                            }
                        )
                    }
                }
            }
            Spacer(Modifier.width(2.dp))
//            if (inRoute) {
//                RoundedSideIconButton(
//                    shape = RoundedCornerShape(topEnd = 60.dp, bottomEnd = 60.dp),
//                    iconPainter = painterResource(Res.drawable.right_icon),
//                    onClick = { onPrevClick() })
//                Spacer(Modifier.width(4.dp))
//            }
        }

    }

}


@Composable
fun TabText(modifier: Modifier = Modifier, text: String = "Milk") {
    Text(text, modifier.padding(8.dp), color = MaterialTheme.colorScheme.onSurface)
}

@Preview
@Composable
fun MilkTransaction(
    modifier: Modifier = Modifier,
    user: CustomerEntity? = null,
    onSubmit: (MilkTransactionEntity) -> Unit = {},
    onSkipClick: () -> Unit = {},
    existing: MilkTransactionEntity? = null,
    isEditing: Boolean = false,
    routeMap: CustomerRouteEntity? = null,
    dateOverride: LocalDate? = null
) {
    var priceMode by rememberSaveable { mutableStateOf(PriceMode.FixPrice) }
    var transactionType by rememberSaveable { mutableStateOf(TransactionTypeConstants.Milk.BUY) }
    var pricePerLiterInput by rememberSaveable { mutableStateOf("") }
    var quantityInput by rememberSaveable { mutableStateOf("") }
    var snfInput by rememberSaveable { mutableStateOf("") }
    var snfPriceInput by rememberSaveable { mutableStateOf("") }

    // Initialize from existing data if provided
    LaunchedEffect(existing) {
        existing?.let { e ->
            priceMode = if (e.fixPrice) PriceMode.FixPrice else PriceMode.SnfPrice
            transactionType = e.transactionType
            pricePerLiterInput = if (e.fixPrice) e.fixPriceValue.toString() else ""
            quantityInput = e.quantity.toString()
            snfInput = if (!e.fixPrice) e.snfValue.toString() else ""
            snfPriceInput = if (!e.fixPrice) e.snfPrice.toString() else ""
        }
    }

    // If no existing tx, derive BUY/SELL from selected customer; fallback to BUY
    LaunchedEffect(user, existing) {
        if (existing == null) {
            val t = when (user?.customerType) {
                UserTypeConstants.BUYER -> TransactionTypeConstants.Milk.BUY
                UserTypeConstants.SELLER -> TransactionTypeConstants.Milk.SELL
                else -> TransactionTypeConstants.Milk.BUY
            }
            transactionType = t
        }
    }

    val today: LocalDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
    // Derive initial date from existing if possible (expects yyyy-MM-dd...)
    val existingDate: LocalDate? = existing?.dateTimeStamp?.let { dt ->
        runCatching { dt.substring(0, 10).toLocalDate() }.getOrNull()
    }
    val fromRoute: LocalDate? = routeMap?.date?.let { dt -> runCatching { dt.substring(0, 10).toLocalDate() }.getOrNull() }
    val initialDate = dateOverride ?: fromRoute ?: existingDate
    var txDate by remember { mutableStateOf(initialDate) }

    // Selected customer comes from parent; reflect directly in UI
    val displayUserName = existing?.userName ?: (user?.userName ?: "")

    val totalAmount = PricingUtils.calculateMilkTotal(
        mode = priceMode,
        pricePerLiter = pricePerLiterInput.toDoubleOrNull(),
        quantity = quantityInput.toDoubleOrNull(),
        snf = snfInput.toDoubleOrNull(),
        snfPrice = snfPriceInput.toDoubleOrNull()
    )
    val totalText = if (totalAmount > 0.0) totalAmount.formatMoney() else ""
    Surface(
        modifier = modifier.padding(top = 6.dp, bottom = 8.dp).padding(horizontal = 8.dp)
            .border(1.dp, MaterialTheme.colorScheme.primary, roundCorner())
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
//                if (displayUserName.isBlank()) {
//                    Column(Modifier.weight(1f)) {
//                        AutoCompleteDropdown(
//                            items = searchResults.value.searchResults,
//                            query = searchQuery,
//                            onQueryChange = { searchQuery = it },
//                            itemContent = { c -> Text(c.userName) },
//                            onItemSelected = { c ->
//                                selectedCustomer = c
//                                vm.selectCustomer(c)
//                                searchQuery = c.userName
//                            }
//                        )
//                    }
//                } else {

                    key(displayUserName) {
                        ValidatedInputField(
                            modifier = Modifier.weight(1f),
                            hintText = "Name",
                            initialValue = displayUserName?:"",
                            disabled = true,
                        )
                    }
//                }
                Spacer(Modifier.width(8.dp))
                BuySellSwitch(value = transactionType, onChange = { transactionType = it })

            }
            Spacer(Modifier.height(10.dp))
            Row(modifier = Modifier.height(54.dp), verticalAlignment = Alignment.CenterVertically) {
                SelectableOptionWithCheckbox(
                    modifier = Modifier.weight(1f),
                    title = "Fix Price",
                    selected = priceMode == PriceMode.FixPrice,
                    checked = priceMode == PriceMode.FixPrice,
                    onClick = { priceMode = PriceMode.FixPrice },
                    onCheckedChange = { priceMode = PriceMode.FixPrice }
                )
                Spacer(Modifier.width(10.dp))
                SelectableOptionWithCheckbox(
                    modifier = Modifier.weight(1f),
                    title = "By SNF Price",
                    selected = priceMode == PriceMode.SnfPrice,
                    checked = priceMode == PriceMode.SnfPrice,
                    onClick = { priceMode = PriceMode.SnfPrice },
                    onCheckedChange = { priceMode = PriceMode.SnfPrice }
                )

            }
            if (priceMode == PriceMode.SnfPrice) {
                // Row: SNF and SNF Price
                Row(verticalAlignment = Alignment.CenterVertically) {
                    ValidatedInputField(
                        Modifier.weight(1f),
                        title = "SNF",
                        initialValue = snfInput,
                        keyboardType = KeyboardType.Decimal,
                        onValidationChanged = { _, v -> snfInput = v }
                    )
                    ValidatedInputField(
                        Modifier.weight(1f),
                        title = "SNF Price",
                        initialValue = snfPriceInput,
                        keyboardType = KeyboardType.Decimal,
                        onValidationChanged = { _, v -> snfPriceInput = v }
                    )
                }
                // Full-width Quantity
                Row(verticalAlignment = Alignment.CenterVertically) {
                    ValidatedInputField(
                        Modifier.weight(1f),
                        title = "Quantity",
                        initialValue = quantityInput,
                        keyboardType = KeyboardType.Decimal,
                        onValidationChanged = { _, v -> quantityInput = v }
                    )
                    DateInputField(
                        modifier = Modifier.weight(1f).padding(start = 8.dp),
                        title = "Date",
                        hintText = "Select date",
                        initialDate = txDate,
                        onDateChanged = { txDate = it }
                    )
                }
            } else {
                // FixPrice mode: Full-width Price/Ltr, then Quantity
                Row(verticalAlignment = Alignment.CenterVertically) {
                    ValidatedInputField(
                        Modifier.weight(1f),
                        title = "Price/Ltr",
                        initialValue = pricePerLiterInput,
                        keyboardType = KeyboardType.Decimal,
                        onValidationChanged = { _, v -> pricePerLiterInput = v }
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    ValidatedInputField(
                        Modifier.weight(1f),
                        title = "Quantity",
                        initialValue = quantityInput,
                        keyboardType = KeyboardType.Decimal,
                        onValidationChanged = { _, v -> quantityInput = v }
                    )
                    DateInputField(
                        modifier = Modifier.weight(1f).padding(start = 8.dp),
                        title = "Date",
                        hintText = "Select date",
                        initialDate = txDate,
                        onDateChanged = { txDate = it }
                    )
                }
            }

            // Disabled Total field (full width), updates with inputs
            key(totalText) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    ValidatedInputField(
                        Modifier.weight(1f),
                        title = "Total",
                        initialValue = totalText,
                        disabled = true,
                        keyboardType = KeyboardType.Decimal
                    )
                }
            }

            Spacer(Modifier.height(10.dp))
            val hasUser = displayUserName?.isNotBlank()
            val hasQuantity = quantityInput.isNotBlank()
            val hasDate = txDate != null
            val priceFieldsOk = when (priceMode) {
                PriceMode.FixPrice -> pricePerLiterInput.isNotBlank()
                PriceMode.SnfPrice -> snfInput.isNotBlank() && snfPriceInput.isNotBlank()
            }
            val canSave = hasUser?:false && hasQuantity && hasDate && priceFieldsOk

            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                OutlineActionButton(
                    modifier = Modifier.weight(1f).heightIn(52.dp),
                    text = if (isEditing) "Cancel" else "Skip",
                    borderColor = MaterialTheme.colorScheme.error,
                ) { onSkipClick() }

                Spacer(Modifier.width(10.dp))

                FilledActionButton(
                    modifier = Modifier.weight(1f).heightIn(52.dp),
                    text = if (isEditing) "Update" else "Save",
                    fillColor = AppColors.Green,
                    enabled = canSave
                ) {
                    val userId = user?.userId ?: existing?.userId ?: 0L
                    val entity = MilkTransactionEntity(
                        id = existing?.id ?: 0L,
                        userId = userId,
                        userName = displayUserName,
                        dateTimeStamp = (txDate ?: Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date).toString(),
                        editedOn = null,
                        quantity = quantityInput.toDoubleOrNull() ?: 0.0,
                        transactionType = transactionType,
                        fixPrice = priceMode == PriceMode.FixPrice,
                        snfValue = snfInput.toDoubleOrNull() ?: 0.0,
                        snfPrice = snfPriceInput.toDoubleOrNull() ?: 0.0,
                        fixPriceValue = pricePerLiterInput.toDoubleOrNull() ?: 0.0,
                        total = totalAmount
                    )
                    onSubmit(entity)
                }
            }

        }
    }

}

@Preview
@Composable
fun MoneyTransaction(
    modifier: Modifier = Modifier,
    user: CustomerEntity? = null,
    onSubmit: (MoneyTransactionEntity) -> Unit = {},
    onSkipClick: () -> Unit = {},
    existing: MoneyTransactionEntity? = null,
    isEditing: Boolean = false
) {
    var transactionType by rememberSaveable { mutableStateOf(TransactionTypeConstants.Money.RECEIVED) }
    var amountInput by rememberSaveable { mutableStateOf("") }
    var noteInput by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(existing) {
        existing?.let { e ->
            transactionType = e.transactionType
            amountInput = e.amount.toString()
            noteInput = e.note ?: ""
        }
    }

    val displayUserName = existing?.userName ?: (user?.userName ?: "")

    // Handle date similar to Milk form
    val existingMoneyDate: LocalDate? = existing?.dateTimeStamp?.let { dt ->
        runCatching { dt.substring(0, 10).toLocalDate() }.getOrNull()
    }
    var moneyDate by remember { mutableStateOf(existingMoneyDate) }

    Surface(
        modifier = modifier.padding(top = 6.dp, bottom = 8.dp).padding(horizontal = 8.dp)
            .border(1.dp, MaterialTheme.colorScheme.primary, roundCorner())
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            // Name in its own row; key to refresh when user changes
            key(displayUserName) {
                ValidatedInputField(
                    modifier = Modifier.fillMaxWidth(),
                    hintText = "Name",
                    initialValue = displayUserName,
                    disabled = true,
                )
            }
            Spacer(Modifier.height(10.dp))
            // Paid / Received toggle with same height as Fix Price / By SNF
            Row(modifier = Modifier.height(54.dp).padding(horizontal = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                SelectableOptionWithCheckbox(
                    modifier=Modifier.fillMaxWidth(0.5f),
                    title = "Paid",
                    selected = transactionType == TransactionTypeConstants.Money.PAID,
                    checked = transactionType == TransactionTypeConstants.Money.PAID,
                    onClick = { transactionType = TransactionTypeConstants.Money.PAID },
                    onCheckedChange = { transactionType = TransactionTypeConstants.Money.PAID }
                )
                Spacer(Modifier.width(10.dp))
                SelectableOptionWithCheckbox(
                    modifier=Modifier.fillMaxWidth(5f),
                    title = "Received",
                    selected = transactionType == TransactionTypeConstants.Money.RECEIVED,
                    checked = transactionType == TransactionTypeConstants.Money.RECEIVED,
                    onClick = { transactionType = TransactionTypeConstants.Money.RECEIVED },
                    onCheckedChange = { transactionType = TransactionTypeConstants.Money.RECEIVED }
                )
            }
            Spacer(Modifier.height(10.dp))
            // Amount and Date side by side
            Row(verticalAlignment = Alignment.CenterVertically) {
                ValidatedInputField(
                    Modifier.weight(1f),
                    title = "Amount",
                    initialValue = amountInput,
                    keyboardType = KeyboardType.Decimal,
                    onValidationChanged = { _, v -> amountInput = v }
                )
                DateInputField(
                    modifier = Modifier.weight(1f).padding(start = 8.dp),
                    title = "Date",
                    hintText = "Select date",
                    initialDate = moneyDate,
                    onDateChanged = { moneyDate = it }
                )
            }
            // Note
            ValidatedInputField(
                Modifier.fillMaxWidth(),
                title = "Note",
                initialValue = noteInput,
                onValidationChanged = { _, v -> noteInput = v }
            )

            Spacer(Modifier.height(10.dp))
            val hasUser = displayUserName.isNotBlank()
            val hasAmount = amountInput.isNotBlank()
            val canSave = hasUser && hasAmount
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                OutlineActionButton(
                    modifier = Modifier.weight(1f).heightIn(52.dp),
                    text = "Cancel",
                    borderColor = MaterialTheme.colorScheme.error,
                ) { onSkipClick() }

                Spacer(Modifier.width(10.dp))

                FilledActionButton(
                    modifier = Modifier.weight(1f).heightIn(52.dp),
                    text = if (isEditing) "Update" else "Save",
                    fillColor = AppColors.Green,
                    enabled = canSave
                ) {
                    val userId = user?.userId ?: existing?.userId ?: 0L
                    val entity = MoneyTransactionEntity(
                        id = existing?.id ?: 0L,
                        userId = userId,
                        userName = displayUserName,
                        dateTimeStamp = (moneyDate ?: Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date).toString(),
                        editedOn = null,
                        amount = amountInput.toDoubleOrNull() ?: 0.0,
                        transactionType = transactionType,
                        note = if (noteInput.isBlank()) null else noteInput
                    )
                    onSubmit(entity)
                }
            }
        }
    }
}
