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
            modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.1f)).clickable(onClick = {
                onDismiss.invoke()
            }),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (inRoute) {
                Spacer(Modifier.width(4.dp))
                RoundedSideIconButton(
                    shape = RoundedCornerShape(topStart = 60.dp, bottomStart = 60.dp),
                    iconPainter = painterResource(Res.drawable.left_icon),
                    onClick = { onPrevClick() })

            }
            Spacer(Modifier.width(2.dp))
            Surface(
                modifier = Modifier.padding(6.dp).weight(1f).clickable(false, onClick = {}),
                shape = roundCorner(),
                color = MaterialTheme.colorScheme.background
            ) {
                Column {
                    TabRow(
                        selectedTabIndex = selectedTab,
                        containerColor = MaterialTheme.colorScheme.surface
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
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        CommonInputBox(
                            modifier = Modifier.weight(1f).padding(end = 10.dp),
                            hintText = "Search by Name,Id or Number",
                            onValueChange = {
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
                }
            }
            Spacer(Modifier.width(2.dp))
            if (inRoute) {
                RoundedSideIconButton(
                    shape = RoundedCornerShape(topEnd = 60.dp, bottomEnd = 60.dp),
                    iconPainter = painterResource(Res.drawable.right_icon),
                    onClick = { onPrevClick() })
                Spacer(Modifier.width(4.dp))
            }
        }

    }

}


@Composable
fun TabText(modifier: Modifier = Modifier, text: String = "Milk") {
    Text(text, modifier.padding(8.dp), color = MaterialTheme.colorScheme.onSurface)
}

@Composable
fun MilkTransaction(modifier: Modifier = Modifier) {
    Surface(modifier=modifier) {
        Column {

        }
    }
}
