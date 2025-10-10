package org.example.mandm.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import org.example.mandm.commonComponent.BuyerSellerSwitch
import org.example.mandm.commonComponent.FilledActionButton
import org.example.mandm.commonComponent.OutlineActionButton
import org.example.mandm.commonComponent.ValidatedInputField
import org.example.mandm.commonComponent.PhoneInputWithValidation
import org.example.mandm.roundCorner
import org.example.mandm.UserTypeConstants
import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import org.example.mandm.dataModel.CustomerEntity
import org.koin.compose.viewmodel.koinViewModel
import org.example.mandm.viewModels.CreateCustomerViewModel

@Preview
@Composable
fun CreateUserDialog(
    onDismiss: () -> Unit = {},
    onSaved: (created: CustomerEntity) -> Unit = {}
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnClickOutside = true,
            dismissOnBackPress = true
        )
    ) {
        val vm: CreateCustomerViewModel = koinViewModel()
        val uiState = vm.uiState.collectAsState()

        var name by rememberSaveable { mutableStateOf("") }
        var nickName by rememberSaveable { mutableStateOf("") }
        var userType by rememberSaveable { mutableStateOf(UserTypeConstants.BUYER) }
        var phone by rememberSaveable { mutableStateOf("") }
        var village by rememberSaveable { mutableStateOf("") }

        Row(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.1f))
                .clickable(onClick = { onDismiss.invoke() }),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(Modifier.width(2.dp))
            Surface(
                modifier = Modifier.padding(6.dp).fillMaxWidth(0.96f)
                    .clickable(false, onClick = {}),
                shape = roundCorner(),
            ) {
                Column(Modifier.padding(12.dp)) {
                    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                        Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                            Text(
                                text = "Create User Account",
                                style = MaterialTheme.typography.titleLarge,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        Text(
                            text = "Close",
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.clickable { onDismiss() }
                        )
                    }
                    Spacer(Modifier.height(10.dp))

                    // Username + Buyer/Seller in same row
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        ValidatedInputField(
                            modifier = Modifier.weight(1f),
                            title = "Username",
                            hintText = "Enter user name",
                            onValidationChanged = { _, v -> name = v }
                        )
                        Spacer(Modifier.width(8.dp))
                        Column {
                        Spacer(Modifier.height(22.dp))
                        BuyerSellerSwitch(value = userType, onChange = { userType = it })}
                    }

                    // Optional NickName field
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        ValidatedInputField(
                            modifier = Modifier.weight(1f),
                            title = "NickName (optional)",
                            hintText = "Enter nickname",
                            onValidationChanged = { _, v -> nickName = v }
                        )
                    }

                    // Phone number
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        PhoneInputWithValidation(
                            modifier = Modifier.weight(1f),
                            title = "Phone Number",
                            onValidationChanged = { _, v -> phone = v }
                        )
                    }

                    // Village
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        ValidatedInputField(
                            modifier = Modifier.weight(1f),
                            title = "Village",
                            hintText = "Enter village",
                            onValidationChanged = { _, v -> village = v }
                        )
                    }

                    Spacer(Modifier.height(12.dp))
                    val canSave = name.isNotBlank() && village.isNotBlank() && (uiState.value.isLoading == false)
                    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        FilledActionButton(
                            modifier = Modifier.weight(1f).heightIn(52.dp),
                            text = "Save",
                            enabled = canSave
                        ) {
                            vm.createCustomer(
                                name = name,
                                nickName = nickName.ifBlank { null },
                                userType = userType,
                                phone = phone.ifBlank { null },
                                village = village
                            )
                        }
                    }
                }
            }
            Spacer(Modifier.width(2.dp))
        }
        // Deliver created customer back to parent then dismiss
        LaunchedEffect(Unit) {
            vm.onSaved.collect { created ->
                onSaved(created)
                onDismiss()
            }
        }
    }
}
