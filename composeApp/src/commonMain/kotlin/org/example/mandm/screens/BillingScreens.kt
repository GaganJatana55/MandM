package org.example.mandm.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.datetime.toLocalDateTime
import org.example.mandm.dataModel.BillEntity
import org.example.mandm.viewModels.BillingViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun BillListScreen(
    userId: Long,
    userName: String,
    viewModel: BillingViewModel = koinViewModel(),
    onShareText: (String) -> Unit = {}
) {
    LaunchedEffect(userId) { viewModel.loadForUser(userId) }
    val state = viewModel.ui.value
    Column(Modifier.padding(12.dp)) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = "Bills - $userName", style = MaterialTheme.typography.titleMedium)
            Button(onClick = {
                val today = kotlinx.datetime.Clock.System.now()
                    .toLocalDateTime(kotlinx.datetime.TimeZone.currentSystemDefault()).date.toString()
                val now = kotlinx.datetime.Clock.System.now().toEpochMilliseconds().toString()
                viewModel.generateBill(userId, userName, endDate = today, createdOn = now)
            }) { Text("Generate Bill") }
        }
        LazyColumn {
            items(state.bills) { bill -> BillRow(
                bill = bill,
                onSettle = { r, p -> viewModel.settleBill(bill, r, p) },
                onShare = { onShareText(viewModel.buildShareText(bill)) }
            ) }
        }
    }
}

@Composable
private fun BillRow(
    bill: BillEntity,
    onSettle: (Double, Double) -> Unit,
    onShare: () -> Unit
) {
    var received by remember { mutableStateOf(0.0) }
    var paid by remember { mutableStateOf(0.0) }
    Column(Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
        Text("${bill.startDate} - ${bill.endDate}")
        Text("Milk: ${bill.totalMilkQuantity} L | Amount: ${bill.totalMilkAmount}")
        Text("Money: R ${bill.moneyReceivedInRange} | P ${bill.moneyPaidInRange}")
        Text("Carry: ${bill.carryForwardAmount} | Pending: ${bill.pendingAmount} | ${bill.status}")
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(
                value = if (received == 0.0) "" else received.toString(),
                onValueChange = { received = it.toDoubleOrNull() ?: 0.0 },
                modifier = Modifier.weight(1f),
                label = { Text("Receive") }
            )
            OutlinedTextField(
                value = if (paid == 0.0) "" else paid.toString(),
                onValueChange = { paid = it.toDoubleOrNull() ?: 0.0 },
                modifier = Modifier.weight(1f),
                label = { Text("Pay") }
            )
            Button(onClick = { onSettle(received, paid) }) { Text("Settle") }
            Button(onClick = { onShare() }) { Text("Share") }
        }
    }
}


