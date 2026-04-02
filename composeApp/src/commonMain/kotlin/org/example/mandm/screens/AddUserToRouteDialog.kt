package org.example.mandm.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import org.example.mandm.commonComponent.CommonDefaultDividerHorizontal
import org.example.mandm.commonComponent.ValidatedInputField
import org.example.mandm.dataModel.CustomerEntity

@Composable
fun AddUserToRouteDialog(
    index: Int=0,
    allCustomers: List<CustomerEntity>, // Your full customer list from Room
    onDismiss: () -> Unit,
    onConfirm: (List<CustomerEntity>,index:Int) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    val selectedCustomers = remember { mutableStateListOf<CustomerEntity>() }

    // Filter the list based on search text
    val filteredList = allCustomers.filter {
        it.userName.contains(searchQuery, ignoreCase = true)
    }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.8f),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {


            Column(modifier = Modifier) {
                // --- TOP BAR: SEARCH & CLOSE ---
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(horizontal = 8.dp)) {
                    ValidatedInputField(
                        modifier = Modifier.weight(1f),
                        hintText = "Search by Name",
                        initialValue = "",
                        onValidationChanged = { _, v ->
                            searchQuery = v
                        }
                    )
Spacer(Modifier.width(4.dp))
                    Text(text = "close", fontWeight = FontWeight.Medium, fontSize = 16.sp, color =MaterialTheme.colorScheme.error , modifier = Modifier.clickable { onDismiss() })
                }
                CommonDefaultDividerHorizontal()


                // --- MIDDLE: SCROLLABLE LIST ---
                LazyColumn(modifier = Modifier.weight(1f).padding(vertical = 4.dp, horizontal = 8.dp)) {

                    items(items = filteredList) { customer ->
                        val isSelected = selectedCustomers.contains(customer)

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    if (isSelected) selectedCustomers.remove(customer)
                                    else selectedCustomers.add(customer)
                                }
                                .padding(vertical = 6.dp, horizontal = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                            Text(customer.userName)
                            Text("${customer.nickName} - ${customer.village}", fontSize = 14.sp, fontWeight = FontWeight.Normal)

                            }

                            // Checkbox with the "Count" or Selection Tick
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.size(24.dp)
                                    .border(1.dp, color = MaterialTheme.colorScheme.onSurface)
                                    .background(if (isSelected) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.surface)
                            ) {

                                if (isSelected) {

                                    Text(
                                        text = "${selectedCustomers.indexOf(customer) + 1}",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.SemiBold,
                                        color = MaterialTheme.colorScheme.surface,
                                        modifier = Modifier.offset(y = (-2).dp)
                                    )
                                }
                            }
                        }
                        CommonDefaultDividerHorizontal()
                    }
                }

                // --- BOTTOM: CONFIRM BUTTON ---
                Button(
                    onClick = { onConfirm(selectedCustomers.toList(),index) },
                    modifier = Modifier.fillMaxWidth().padding(12.dp).height(52.dp),
                    enabled = selectedCustomers.isNotEmpty(),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE67E22)) // Milkman orange
                ) {
                    Text("Add ${selectedCustomers.size} to Route")
                }
            }
        }
    }
}