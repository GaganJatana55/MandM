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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import mandm.composeapp.generated.resources.Res
import mandm.composeapp.generated.resources.add_user_icon
import mandm.composeapp.generated.resources.left_icon
import mandm.composeapp.generated.resources.right_icon
import org.example.mandm.commonBorder
import org.example.mandm.commonComponent.RoundedSideIconButton
import org.example.mandm.commonComponent.CommonInputBox
import org.example.mandm.PriceMode
import org.example.mandm.commonComponent.CustomColoredCheckbox
import org.example.mandm.commonComponent.SelectableOptionWithCheckbox
import org.example.mandm.commonComponent.TextInputWithTitle
import org.example.mandm.commonComponent.ValidatedInputField
import org.example.mandm.dataModel.User
import org.example.mandm.dataModel.dummyUser
import org.example.mandm.roundCorner
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

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
    onSkipClick: () -> Unit = {}

) {
    Dialog(
        onDismissRequest = onDismiss, properties = DialogProperties(
            usePlatformDefaultWidth = false, dismissOnClickOutside = true, dismissOnBackPress = true
        )
    ) {
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
                        ValidatedInputField(
                            modifier = Modifier.weight(1f),
                            hintText = "Search by Name,Id or Number",
                            onValidationChanged = { valid, value ->
//validations and value
                            })
                        Image(
                            modifier = Modifier.size(42.dp)
                                .background(color = MaterialTheme.colorScheme.surface)
                                .commonBorder().padding(4.dp),
                            painter = painterResource(Res.drawable.add_user_icon),
                            contentDescription = "Add user Button",
                            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
                        )

                    }
                    MilkTransaction(Modifier.padding(top = 8.dp))
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
fun MilkTransaction(modifier: Modifier = Modifier, user: User = dummyUser) {
    var priceMode by rememberSaveable { mutableStateOf(PriceMode.FixPrice) }
    Surface(
        modifier = modifier.padding(top = 6.dp, bottom = 8.dp).padding(horizontal = 8.dp)
            .border(1.dp, MaterialTheme.colorScheme.primary, roundCorner())
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Row {
                ValidatedInputField(
                    modifier = Modifier.weight(1f),
                    hintText = "Name",
                    initialValue = user.name,
                    disabled = true,
                )

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
                Row(verticalAlignment = Alignment.CenterVertically) {
                    ValidatedInputField(
                        Modifier.weight(1f),
                        title = "SNF",
                        initialValue = "",
                        keyboardType = KeyboardType.Decimal
                    )
                    ValidatedInputField(
                        Modifier.weight(1f),
                        title = "SNF Price",
                        initialValue = "",
                        keyboardType = KeyboardType.Decimal
                    )
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                ValidatedInputField(
                    Modifier.weight(1f),
                    title = "Price/Ltr",
                    initialValue = "",
                    keyboardType = KeyboardType.Decimal
                )
                ValidatedInputField(
                    Modifier.weight(1f),
                    title = "Quantity",
                    initialValue = "",
                    disabled = true,
                    keyboardType = KeyboardType.Decimal
                )
            }

        }
    }

}
