package org.example.mandm.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.key
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import kotlinx.coroutines.flow.MutableSharedFlow
import org.example.mandm.viewModels.CustomerViewModel
import org.koin.compose.viewmodel.koinViewModel

// Home Tab
object HomeTab : Tab {
    override val options: TabOptions
        @Composable get() = TabOptions(index = 0u, title = "Home")
    private val resetFlow = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    fun requestResetToRoot() { resetFlow.tryEmit(Unit) }

    private var version by mutableStateOf(0)
    fun resetNextEnter() { version++ }

    @Composable
    override fun Content() {
        DailyRouteData()
    }
}

class HomeLandingScreen : Screen {
    @Composable
    override fun Content() {
        DailyRouteData()
    }
}

// Settings Tab
object SettingsTab : Tab {
    override val options: TabOptions
        @Composable get() = TabOptions(index = 1u, title = "Settings")
    private val resetFlow = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    fun requestResetToRoot() { resetFlow.tryEmit(Unit) }

    private var version by mutableStateOf(0)
    fun resetNextEnter() { version++ }

    @Composable
    override fun Content() {
        SignInUI()
    }
}

class SettingsLandingScreen : Screen {
    @Composable
    override fun Content() {
        SignInUI()
    }
}

// Summary Tab
object SummaryTab : Tab {
    override val options: TabOptions
        @Composable get() = TabOptions(index = 2u, title = "Summary")
    private val resetFlow = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    fun requestResetToRoot() { resetFlow.tryEmit(Unit) }

    private var version by mutableStateOf(0)
    fun resetNextEnter() { version++ }

    @Composable
    override fun Content() {
        SummaryLandingScreen().Content()
    }
}

class SummaryLandingScreen : Screen {
    @Composable
    override fun Content() {
        val vm: CustomerViewModel = koinViewModel()
        LaunchedEffect(Unit) { vm.observeAllCustomers() }
        val state = vm.uiState.collectAsState()
        UsersList(users = state.value.customers)
    }
}

// Users Tab
object UsersTab : Tab {
    override val options: TabOptions
        @Composable get() = TabOptions(index = 3u, title = "Users")
    private val resetFlow = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    fun requestResetToRoot() { resetFlow.tryEmit(Unit) }

    private var version by mutableStateOf(0)
    fun resetNextEnter() { version++ }

    @Composable
    override fun Content() {
        UsersLandingScreen().Content()
    }
}

class UsersLandingScreen : Screen {
    @Composable
    override fun Content() {
        val vm: CustomerViewModel = koinViewModel()
        LaunchedEffect(Unit) { vm.observeAllCustomers() }
        val state = vm.uiState.collectAsState()
        UserListHome(users = state.value.customers)
    }
}


