package org.example.mandm.screens

import ThreeWaySwitch
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mandm.composeapp.generated.resources.Res
import mandm.composeapp.generated.resources.calendar_icon
import mandm.composeapp.generated.resources.left_icon
import mandm.composeapp.generated.resources.right_icon
import org.example.mandm.TransactionTypeConstants
import org.example.mandm.backgroundCommonCard
import org.example.mandm.commonComponent.GetCommonScaffoldForTabs
import org.example.mandm.commonComponent.GetDivider
import org.example.mandm.commonComponent.TopBar
import org.example.mandm.dataModel.MilkTrans
import org.example.mandm.dataModel.sampleTransactions
import org.example.mandm.formatMoney
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun DailyData(modifier: Modifier = Modifier) {
    val scrollState = rememberScrollState()
    GetCommonScaffoldForTabs(modifier = modifier, topBar = {

        Box(
            modifier = Modifier.fillMaxWidth()
                .padding(WindowInsets.statusBars.asPaddingValues()),
            contentAlignment = Alignment.Center,

            ) { ThreeWaySwitch(0) { } }

    }) {
        DaySelector()
        GetDivider(Modifier.height(0.5.dp))
        HorizontalDivider()
        LazyColumn(Modifier.scrollable(scrollState, orientation = Orientation.Vertical)) {

            items(
                sampleTransactions
            ) {
                DailyItem(transaction = it)
            }

        }
    }


}


@Preview
@Composable
fun DailyItem(
    modifier: Modifier = Modifier, transaction: MilkTrans = MilkTrans(
        type = TransactionTypeConstants.BUY,
        userId = 1,
        userName = "Ramesh",
        fixPrice = true,
        quantity = 5.0,
        SNF = -1.0,
        date = "2025-08-01",
        time = 1722477600000 // e.g., "2025-08-01 06:00 AM UTC"
    )
) {
    Column(
        modifier = modifier.fillMaxWidth().backgroundCommonCard()
            .padding(8.dp)
    ) {
        Row(
            modifier.fillMaxWidth().backgroundCommonCard(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                modifier = Modifier.padding(start = 10.dp),
                text = transaction.userName,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold
            )

            Text(
                modifier = Modifier.padding(end = 10.dp),
                text = if (transaction.type == TransactionTypeConstants.BUY) {
                    "BUY"
                } else {
                    "SELL"
                },
                style = MaterialTheme.typography.bodyMedium,
                color = if (transaction.type == TransactionTypeConstants.BUY) Color.Red else Color.Green,
                fontWeight = FontWeight.SemiBold
            )
        }

        Row(
            modifier.fillMaxWidth().backgroundCommonCard()
                .padding(top = 6.dp), horizontalArrangement = Arrangement.SpaceAround
        ) {
            TextWithUnit(
                Modifier.padding(end = 10.dp).widthIn(50.dp),
                value = transaction.quantity.toString(),
                unitString = "Ltr."
            )
            TextWithUnit(
                Modifier.padding(end = 10.dp).widthIn(70.dp),
                value = if (transaction.fixPrice) {
                    transaction.price.toString()
                } else {
                    transaction.SNFPrice.toString()
                },
                unitString = if (transaction.fixPrice) {
                    "Per/Ltr"
                } else {
                    "SNF Price"
                }
            )
            TextWithUnit(
                Modifier.padding(end = 10.dp).widthIn(70.dp),
                value = if (transaction.fixPrice) {
                    "--.--"
                } else {
                    transaction.SNF.toString()
                },
                unitString = if (transaction.fixPrice) {
                    "Per Ltr."
                } else {
                    "SNF"
                }
            )

            TextWithUnit(
                Modifier.padding(end = 10.dp).widthIn(80.dp),
                value = if (transaction.fixPrice) {
                    (transaction.quantity * transaction.price).formatMoney()
                } else {
                    (transaction.SNF * transaction.SNFPrice * transaction.quantity).formatMoney()
                },
                unitString = ""
            )

        }
        Spacer(Modifier.height(12.dp))
        GetDivider()
    }
}

@Composable
fun TextWithUnit(modifier: Modifier = Modifier, value: String = "5.0", unitString: String = "Ltr") {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = value,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(Modifier.width(4.dp))
        Text(
            text = unitString,
            style = MaterialTheme.typography.labelSmall,
            fontSize = 10.sp,
            fontWeight = FontWeight.Normal
        )
    }
}


@Preview
@Composable
fun DaySelector(modifier: Modifier = Modifier, text: String = "Today") {
    Surface(
        modifier = modifier.fillMaxWidth().padding(top = 8.dp).padding(horizontal = 8.dp)

    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(Res.drawable.left_icon),
                contentDescription = "Left Arrow",
                modifier = Modifier.size(18.dp),
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface)

            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(Res.drawable.calendar_icon),
                    contentDescription = "calendar ",
                    modifier = Modifier.size(32.dp),
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface)
                )
                Text(
                    modifier = Modifier.padding(start = 6.dp),
                    text = text,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }
            Image(
                painter = painterResource(Res.drawable.right_icon),
                contentDescription = "Left Arrow",
                modifier = Modifier.size(18.dp),
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface)
            )
        }
    }
}

//@Preview
//@Composable
//fun SummaryBottom(modifier: Modifier= Modifier){
//    Surface(modifier.fillMaxWidth().paddingCommon().backgroundCommonCard(), shape = roundCorner()) {
//Row(Modifier.fillMaxWidth().padding(8.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Bottom) {
//    Column {
//SummaryText(label = "Sold", value = "1200", unitString = "Ltr" )
//SummaryText(label = "Total", value = "35000.12", unitString = "Rs" )}
//    Column {
//SummaryText(label = "Buy", value = "1200", unitString = "Ltr" )
//SummaryText(label = "Total", value = "35000.12", unitString = "Rs" )}
//
//
//}
//    }
//
//}
//
//
//@Composable
//fun SummaryText(modifier: Modifier = Modifier, label:String="Label",value: String = "5.0", unitString: String = "Ltr") {
//    Row(modifier = modifier, verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.Center) {
//        Text(
//            text = label+" : ",
//            style = MaterialTheme.typography.titleMedium,
//            fontWeight = FontWeight.SemiBold
//        )
//        Spacer(Modifier.width(4.dp))
//        Text(
//            text = value,
//            style = MaterialTheme.typography.titleLarge,
//            fontWeight = FontWeight.SemiBold
//        )
//        Spacer(Modifier.width(4.dp))
//        Text(
//            text = unitString,
//            style = MaterialTheme.typography.titleSmall,
//            fontSize = 10.sp,
//            fontWeight = FontWeight.Normal
//        )
//    }
//}