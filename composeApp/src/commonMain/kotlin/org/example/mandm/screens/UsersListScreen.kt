package org.example.mandm.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import org.example.mandm.viewModels.CustomerViewModel
import org.koin.compose.viewmodel.koinViewModel


@Composable
     fun UsersListScreen() {
        val vm: CustomerViewModel = koinViewModel()
        LaunchedEffect(Unit) { vm.observeAllCustomers() }
        val state = vm.uiState.collectAsState()
        UserListHome(users = state.value.customers)
    }



