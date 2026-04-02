package org.example.mandm.screens

import MonthSelector
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mandm.composeapp.generated.resources.Res
import mandm.composeapp.generated.resources.milk
import mandm.composeapp.generated.resources.money
import org.example.mandm.DateTimeUtil
import org.example.mandm.DateTimeUtil.toLocalDateTime
import org.example.mandm.TransactionTypeConstants
import org.example.mandm.backgroundCommonCard
import org.example.mandm.commonComponent.ButtonRoundCorner
import org.example.mandm.commonComponent.CommonSurfaceCard
import org.example.mandm.commonComponent.FilledActionButton
import org.example.mandm.commonComponent.GetCommonScaffoldWithColumnCenter
import org.example.mandm.commonComponent.CommonDefaultDividerHorizontal
import org.example.mandm.dataModel.BalanceUiState
import org.example.mandm.dataModel.LedgerItem
import org.example.mandm.dataModel.MilkTransactionWithLogs
import org.example.mandm.dataModel.MoneyTransactionWithLogs
import org.example.mandm.dataModel.MonthlyLedgerSummary
import org.example.mandm.formatMoney
import org.example.mandm.theme.AppColors
import org.example.mandm.viewModels.BillingViewModel
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import kotlin.time.ExperimentalTime

@Preview
@Composable
fun BillListScreen(
    userId: Long = 11,
    userName: String = "Name",
) {
    val viewModel: BillingViewModel = koinViewModel()
    LaunchedEffect(userId) { viewModel.loadForUser(userId) }
    val state = viewModel.ui.value
    GetCommonScaffoldWithColumnCenter(modifier = Modifier.fillMaxHeight(), topBar = {
        Row(modifier = Modifier.padding(6.dp)) {
            Text(
                text = "Bills - $userName",
                Modifier.padding(top = 8.dp, start = 8.dp),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.SemiBold,
                style = MaterialTheme.typography.titleMedium,
                fontSize = 20.sp
            )
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.TopEnd) {
                ButtonRoundCorner(text = "Generate Bill") {
                }
            }

        }

    }) {

        AllStats()
        Spacer(Modifier.height(12.dp))
        List(userId = userId)
    }

}

@OptIn(ExperimentalTime::class)
@Preview
@Composable
fun List(modifier: Modifier = Modifier, userId: Long) {

    val viewModel: BillingViewModel = koinViewModel()

    // YearMonth, not just month
    var selectedYearMonth by remember {
        mutableStateOf(DateTimeUtil.getCurrentYearMonth())
    }

    val ledgerList by viewModel.ledger.collectAsState()

    // Set user only when it changes


    CommonSurfaceCard {
        Column(modifier = modifier) {
            MonthSelector(selectedYearMonth) { newYearMonth ->
                selectedYearMonth = newYearMonth
                viewModel.setYearMonth(newYearMonth)
            }
            CommonDefaultDividerHorizontal()
            LazyColumn {
                item { SelectedMonthStats(Modifier, viewModel) }
                item {
                    Spacer(Modifier.height(4.dp))
                }
                item{CommonDefaultDividerHorizontal()}
                items(
                    items = ledgerList,
                    key = { "${it::class}_${it.id}" }
                ) { item ->

                    when (item) {
                        is LedgerItem.Milk -> {
                            MilkRowItem(Modifier, item.data)
                        }

                        is LedgerItem.Money -> {
                            MoneyRowItem(Modifier, item.data)
                        }
                    }
                }
            }
        }

    }
}

@Composable
fun MilkRowItem(
    modifier: Modifier = Modifier,
    transaction: MilkTransactionWithLogs,
    onView: () -> Unit = {}
) {

    val tx = transaction.transaction
    val dateTime = tx.dateTimeStamp.toLocalDateTime()
    val isEdited = transaction.editLogs.isNotEmpty()

    Column(
        modifier = modifier
            .padding(horizontal = 8.dp, vertical = 6.dp)
    ) {
        Row(
            Modifier.backgroundCommonCard()
                .padding(horizontal = 12.dp, vertical = 10.dp)
        )
        {    // 🥛 ICON
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(
                        AppColors.Green.copy(alpha = 0.15f),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    modifier = Modifier.size(20.dp),
                    painter = painterResource(Res.drawable.milk),
                    contentDescription = null
                )
            }

            Spacer(Modifier.width(4.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Row()
                {
                    Text(
                        text = "${dateTime.date}",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    if (isEdited || true) {
                        Spacer(Modifier.width(6.dp))
                        Box(
                            modifier = Modifier
                                .background(
                                    AppColors.Grey.copy(alpha = 0.15f),
                                    shape = RoundedCornerShape(6.dp)
                                )
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "Edited",
                                style = MaterialTheme.typography.labelSmall,
                                color = AppColors.Grey,
                                fontSize = 10.sp
                            )
                        }
                    }
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.End,
                        text = tx.transactionType,
                        color = if (tx.transactionType == TransactionTypeConstants.Milk.BUY)
                            AppColors.Red else AppColors.Green,
                        style = MaterialTheme.typography.labelMedium
                    )
                }


                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (tx.fixPrice) {

                        Text(
                            text = "${tx.quantity} Ltr × ₹${tx.fixPriceValue} / Ltr",
                            style = MaterialTheme.typography.bodyMedium
                        )

                    } else {

                        val effectiveRate = (tx.snfPrice ?: 0.0) * (tx.snfValue ?: 0.0)

                        Column {

                            Text(
                                text = "${tx.quantity} Ltr | SNF: ${tx.snfValue} × ₹${tx.snfPrice}",
                                style = MaterialTheme.typography.bodyMedium
                            )

                            Spacer(Modifier.height(2.dp))

                            Text(
                                text = "Rate: ${effectiveRate.formatMoney()} / Ltr",
                                style = MaterialTheme.typography.labelSmall,
                                color = AppColors.Grey
                            )
                        }
                    }
                    // 💰 AMOUNT (Always fixed position)
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.End,
                        text = "${tx.total}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = if (tx.transactionType == TransactionTypeConstants.Milk.BUY)
                            AppColors.Green else AppColors.Red
                    )
                }
            }

        }
        CommonDefaultDividerHorizontal()
    }
}

@Composable
fun MoneyRowItem(
    modifier: Modifier = Modifier,
    transaction: MoneyTransactionWithLogs
) {

    val tx = transaction.transaction
    val dateTime = tx.dateTimeStamp.toLocalDateTime()
    val isEdited = transaction.editLogs.isNotEmpty()

    Column(
        modifier = modifier
            .padding(horizontal = 8.dp, vertical = 6.dp)
    ) {

        Row(
            Modifier
                .backgroundCommonCard()
                .padding(horizontal = 12.dp, vertical = 10.dp)
        ) {

            // 💰 ICON
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(
                        AppColors.Orange.copy(alpha = 0.15f),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    modifier = Modifier.size(20.dp),
                    painter = painterResource(Res.drawable.money),
                    contentDescription = null
                )
            }

            Spacer(Modifier.width(4.dp))

            Column(
                modifier = Modifier.fillMaxWidth()
            ) {

                // 🔹 FIRST ROW (Date + Edited + Type)
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        text = "${dateTime.date}",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold
                    )

                    if (isEdited) {
                        Spacer(Modifier.width(6.dp))
                        Box(
                            modifier = Modifier
                                .background(
                                    AppColors.Grey.copy(alpha = 0.15f),
                                    shape = RoundedCornerShape(6.dp)
                                )
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "Edited",
                                style = MaterialTheme.typography.labelSmall,
                                color = AppColors.Grey,
                                fontSize = 10.sp
                            )
                        }
                    }

                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.End,
                        text = tx.transactionType,
                        color = if (tx.transactionType == "Received")
                            AppColors.Green else AppColors.Red,
                        style = MaterialTheme.typography.labelMedium
                    )
                }

                Spacer(Modifier.height(4.dp))

                // 🔹 SECOND ROW (Note + Amount)
                Row(
                    verticalAlignment = Alignment.Top
                ) {

                    Text(
                        modifier = Modifier.weight(1f),
                        text = tx.note ?: "Note not available",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = if (!tx.note.isNullOrEmpty()) FontWeight.SemiBold else FontWeight.Light,
                        color = if (!tx.note.isNullOrEmpty()) AppColors.Black else AppColors.Grey,
                        maxLines = 1,
                        fontSize = if (!tx.note.isNullOrEmpty()) 14.sp else 10.sp,
                        overflow = TextOverflow.Ellipsis
                    )

                    Text(
                        text = "${tx.amount}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = if (tx.transactionType == "Received")
                            AppColors.Green else AppColors.Red
                    )
                }
            }
        }

        CommonDefaultDividerHorizontal()
    }
}

@Composable
fun MilkRowItemOld(
    modifier: Modifier = Modifier,
    transaction: MilkTransactionWithLogs,
    onView: () -> Unit = {}
) {

    val tx = transaction.transaction
    val dateTime = tx.dateTimeStamp.toLocalDateTime()
    val isEdited = transaction.editLogs.isNotEmpty()

    Column(
        modifier = modifier
            .padding(horizontal = 8.dp, vertical = 6.dp)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .backgroundCommonCard()
                .padding(start = 8.dp, end = 12.dp).padding(top = 4.dp, bottom = 2.dp),
            verticalAlignment = Alignment.Top
        )
        {

            // 🥛 ICON
            Box(
                modifier = Modifier.size(32.dp)
                    .background(
                        AppColors.Green.copy(alpha = 0.15f),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    modifier = Modifier.size(32.dp),
                    painter = painterResource(Res.drawable.milk),
                    contentDescription = "Next Month"
                )
            }



            Column(modifier = Modifier.weight(1f).padding(start = 8.dp)) {

                // DATE + TYPE
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                )
                {
                    Row {
                        Text(
                            text = "${dateTime.date}",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                        if (isEdited || true) {
                            Box(
                                modifier = Modifier.padding(horizontal = 8.dp).background(
                                    AppColors.Grey.copy(alpha = 0.15f),
                                    shape = RoundedCornerShape(6.dp)
                                )
                                    .padding(horizontal = 6.dp, vertical = 2.dp)

                            ) {
                                Text(
                                    text = "Edited",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = AppColors.Grey,
                                    fontSize = 10.sp
                                )
                            }
                        }
                    }
                    Text(
                        text = tx.transactionType,
                        color = if (tx.transactionType == TransactionTypeConstants.Milk.BUY)
                            AppColors.Red else AppColors.Green,
                        fontWeight = FontWeight.SemiBold
                    )
                }



                Spacer(Modifier.height(6.dp))

            }
        }


        // VALUES ROW
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        )
        {

            TextWithUnit(
                Modifier.widthIn(60.dp),
                value = tx.quantity.toString(),
                unitString = "Ltr.",
                alphaEnabled = false
            )

            TextWithUnit(
                Modifier.widthIn(80.dp),
                value = if (tx.fixPrice)
                    tx.fixPriceValue.toString()
                else
                    tx.snfPrice.toString(),
                unitString = if (tx.fixPrice) "Per/Ltr" else "SNF Price",
                alphaEnabled = false
            )
            if (!tx.fixPrice) {
                TextWithUnit(
                    Modifier.widthIn(70.dp),
                    value = if (tx.fixPrice) "--.--"
                    else tx.snfValue.toString(),
                    unitString = if (tx.fixPrice) "Per Ltr." else "SNF",
                    alphaEnabled = false
                )
            } else {
                Spacer(Modifier)
            }

            Text(
                text = "${tx.total}",
                style = MaterialTheme.typography.titleMedium,
                color = if (tx.transactionType == TransactionTypeConstants.Milk.BUY)
                    AppColors.Green else AppColors.Red,
                fontWeight = FontWeight.Bold
            )
        }
        CommonDefaultDividerHorizontal()
    }
}

@Composable
fun MoneyRowItemOld(
    modifier: Modifier = Modifier,
    transaction: MoneyTransactionWithLogs
) {

    val tx = transaction.transaction
    val dateTime = tx.dateTimeStamp.toLocalDateTime()
    val isEdited = transaction.editLogs.isNotEmpty()

    Column(
        modifier = modifier
            .padding(horizontal = 8.dp, vertical = 6.dp)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .backgroundCommonCard()
                .padding(start = 8.dp, end = 12.dp)
                .padding(top = 4.dp, bottom = 2.dp),
            verticalAlignment = Alignment.Top
        )
        {

            // 💰 ICON
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(
                        AppColors.Orange.copy(alpha = 0.15f),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    modifier = Modifier.size(32.dp),
                    painter = painterResource(Res.drawable.money),
                    contentDescription = null
                )
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp)
            ) {

                // DATE + TYPE
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {


                    Row {
                        Text(
                            text = "${dateTime.date}",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                        if (isEdited || true) {
                            Box(
                                modifier = Modifier.padding(start = 6.dp)
                                    .background(
                                        AppColors.Grey.copy(alpha = 0.15f),
                                        shape = RoundedCornerShape(6.dp)
                                    )
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "Edited",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = AppColors.Grey,
                                    fontSize = 10.sp
                                )
                            }


                        }
                    }




                    Text(
                        text = tx.transactionType,
                        color = if (tx.transactionType == "Received")
                            AppColors.Green else AppColors.Red,
                        fontWeight = FontWeight.SemiBold
                    )
                }


            }
        }

        // NOTE + AMOUNT ROW (Using milk details space)
        Row(
            modifier = Modifier.fillMaxWidth().padding(start = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            // NOTE takes remaining space
            Text(
                text = tx.note ?: "Note goes here is this fine or not check new skjdkjd",
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f),
                color = AppColors.Grey
            )

            Spacer(Modifier.width(8.dp))

            Text(
                text = "${tx.amount}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = if (tx.transactionType == "Received")
                    AppColors.Green else AppColors.Red
            )
        }

        CommonDefaultDividerHorizontal()
    }
}

@Preview
@Composable
fun AllStats(modifier: Modifier = Modifier, userId: Long = 12) {
    CommonSurfaceCard() {
        Column(
            modifier.padding(vertical = 12.dp, horizontal = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    "Amount Receivable Since :",
                    modifier = Modifier,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                )

                Text(
                    "12 JAN 2026 ",
                    modifier = Modifier,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp
                )

            }
            Text(
                text = "For 500 ltr milk",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp
            )
            Spacer(Modifier.height(10.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "34455.4",
                    modifier = Modifier,
                    textAlign = TextAlign.Center,
                    color = AppColors.Green,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    fontSize = 36.sp
                )
                Spacer(Modifier.width(32.dp))
                FilledActionButton(text = "Settel Account", onClick = {})
            }
            Spacer(Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp),
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    text = "Last Amount Received:",
                    modifier = Modifier,
                    textAlign = TextAlign.Start,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp
                )
                Spacer(Modifier.width(6.dp))
                Text(
                    text = "20300.6",
                    modifier = Modifier,
                    textAlign = TextAlign.Start,
                    color = AppColors.Green,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp
                )

            }
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp),
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    text = "Till Date : 12 Jan 2026",
                    modifier = Modifier,
                    textAlign = TextAlign.Start,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = "For 500 ltr milk",
                    modifier = Modifier,
                    textAlign = TextAlign.Start,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp
                )
            }
        }
    }
}

@Composable
fun SelectedMonthStats(
    modifier: Modifier = Modifier,
    billingViewModel: BillingViewModel
) {

    val summary by billingViewModel.monthlySummary.collectAsState()

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .backgroundCommonCard()
            .padding(12.dp)
    ) {

        BalanceCard(summary)

        Spacer(Modifier.height(10.dp))

        BalanceDivider()

        Spacer(Modifier.height(10.dp))
        MilkMoneySection(summary)

    }
}
@Composable
private fun MilkMoneySection(summary: MonthlyLedgerSummary) {

    Row(
        modifier = Modifier.fillMaxWidth()
    ) {

        // Milk Activity
        Column(
            modifier = Modifier
                .weight(1f).padding(end = 6.dp)
        ) {

            Text(
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                text = "Milk",
                fontWeight = FontWeight.SemiBold
            )

            Spacer(Modifier.height(6.dp))

            if (summary.milk.milkSoldAmount > 0) {
                StatRow(
                    "Sold",
                    summary.milk.milkSoldAmount.formatMoney()
                )
            }

            if (summary.milk.milkBoughtAmount > 0) {
                StatRow(
                    "Bought",
                    summary.milk.milkBoughtQty.formatMoney()
                )
            }
        }

        // Vertical Divider
        Box(
            modifier = Modifier
                .width(1.dp)
                .height(50.dp)
                .background(AppColors.Grey.copy(alpha = 0.3f))
        )

        Spacer(Modifier.width(12.dp))

        // Money Activity
        Column(
            modifier = Modifier
                .weight(1f)
        ) {

            Text(
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                text = "Money",
                fontWeight = FontWeight.SemiBold
            )

            Spacer(Modifier.height(6.dp))

            if (summary.money.moneyReceived > 0) {
                StatRow(
                    "Received",
                    summary.money.moneyReceived.formatMoney()
                )
            }

            if (summary.money.moneyPaid > 0) {
                StatRow(
                    "Paid",
                    summary.money.moneyPaid.formatMoney()
                )
            }
        }
    }
}
@Composable
private fun BalanceCard(summary: MonthlyLedgerSummary) {

    val color = summary.balanceUiState.toColor()

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = summary.displayAmount.formatMoney(),
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = color
        )

        Spacer(Modifier.height(6.dp))

        Text(
            text = when (summary.balanceUiState) {
                BalanceUiState.YOU_GET -> "Client has to pay"
                BalanceUiState.YOU_PAY -> "You have to pay"
                BalanceUiState.SETTLED -> "Settled"
            },
            fontSize = 16.sp,
            color = color
        )
    }
}

@Composable
private fun StatRow(
    title: String,
    value: String
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            text = title,
            fontSize = 14.sp
        )

        Spacer(Modifier.weight(1f))

        Text(
            text = value,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
fun BalanceUiState.toColor(): Color {
    return when (this) {
        BalanceUiState.YOU_GET -> AppColors.Green
        BalanceUiState.YOU_PAY -> AppColors.Red
        BalanceUiState.SETTLED -> AppColors.Grey
    }
}
@Composable
private fun BalanceDivider() {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {

        HorizontalDivider(
            modifier = Modifier.weight(1f),
            color = AppColors.Grey.copy(alpha = 0.4f)
        )

        Text(
            text = "Balance",
            modifier = Modifier.padding(horizontal = 8.dp),
            fontSize = 14.sp,
            color = AppColors.Grey
        )

        HorizontalDivider(
            modifier = Modifier.weight(1f),
            color = AppColors.Grey.copy(alpha = 0.4f)
        )
    }
}
@Composable
fun CompactMonthlySummary(
    viewModel: BillingViewModel,
    modifier: Modifier = Modifier
) {
    val summary by viewModel.monthlySummary.collectAsState()
    val color = summary.balanceUiState.toColor()

    Column(
        modifier = modifier
            .fillMaxWidth()
            .backgroundCommonCard()
            .padding(horizontal = 12.dp, vertical = 10.dp)
    ) {

        // Balance Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column(modifier = Modifier.weight(1f)) {

                Text(
                    text = "Balance",
                    style = MaterialTheme.typography.labelSmall,
                    color = AppColors.Grey
                )

                Text(
                    text = "${summary.displayAmount.formatMoney()}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = color
                )

                Text(
                    text = when (summary.balanceUiState) {
                        BalanceUiState.YOU_GET -> "Client has to pay"
                        BalanceUiState.YOU_PAY -> "You have to pay"
                        BalanceUiState.SETTLED -> "Settled"
                    },
                    fontSize = 12.sp,
                    color = color
                )
            }
        }

        Spacer(Modifier.height(6.dp))

        // Divider
        CommonDefaultDividerHorizontal()

        Spacer(Modifier.height(6.dp))

        // Compact Breakdown Row
        Column {

            if (summary.milk.milkSoldAmount > 0)
                MiniStat("Milk Sold", summary.milk.milkSoldAmount)

            if (summary.milk.milkBoughtAmount > 0)
                MiniStat("Milk Bought", summary.milk.milkBoughtAmount)

            if (summary.money.moneyReceived > 0)
                MiniStat("Money Received", summary.money.moneyReceived)

            if (summary.money.moneyPaid > 0)
                MiniStat("Money Paid", summary.money.moneyPaid)
        }
    }
}

@Composable
private fun MiniStat(
    label: String,
    amount: Double
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium
        )

        Text(
            text = "${amount.formatMoney()}",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.End,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.SemiBold
        )
    }

    Spacer(Modifier.height(2.dp))
}


