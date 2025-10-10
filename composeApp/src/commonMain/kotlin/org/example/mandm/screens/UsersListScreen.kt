package org.example.mandm.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import cafe.adriel.voyager.core.screen.Screen
import org.koin.compose.viewmodel.koinViewModel
import org.example.mandm.viewModels.CustomerViewModel

class UsersListScreen : Screen {
    @Composable
    override fun Content() {
        val vm: CustomerViewModel = koinViewModel()
        LaunchedEffect(Unit) { vm.observeAllCustomers() }
        val state = vm.uiState.collectAsState()
        UserListHome(users = state.value.customers)
    }
}


