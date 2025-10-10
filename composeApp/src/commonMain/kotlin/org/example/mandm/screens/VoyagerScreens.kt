package org.example.mandm.screens

import BottomNavBar
import BottomNavItem
import MilkSellerTheme
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import mandm.composeapp.generated.resources.Res
import mandm.composeapp.generated.resources.add_icon
import org.example.mandm.roundCorner
import org.jetbrains.compose.resources.painterResource

class SignInScreen : Screen {
    @Composable
    override fun Content() {
        SignInUI()
    }
}

data class SignUpScreen(val onComplete: () -> Unit) : Screen {
    @Composable
    override fun Content() {
        SignUpUI(onComplete = onComplete)
    }
}

data class SelectRoleScreen(val onSignUpComplete: () -> Unit) : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        SelectRole(
            onProceedToSignUp = {
                navigator.push(SignUpScreen(onComplete = onSignUpComplete))
            }
        )
    }
}


class CreateUserScreen(
    private val onClose: () -> Unit = {},
    private val onSave: (String, String?, String, String, String) -> Unit = { _, _, _, _, _ -> }
) : Screen {
    @Composable
    override fun Content() {
        CreateUserDialog(
            onDismiss = onClose,

        ) {
        }
    }
}

// Screen wrapper for milk transaction dialog
class MilkTransactionScreen(
    private val onClose: () -> Unit = {},
    private val tabs: Tabs = Tabs.MilkAndMoney,
    private val onSaveClick: () -> Unit = {},
    private val onSkipClick: () -> Unit = {}
) : Screen {
    @Composable
    override fun Content() {
        TransactionDialog(
            onDismiss = onClose,
            showTabs = tabs,
            onSaveClick = onSaveClick,
            onSkipClick = onSkipClick
        )
    }
}


class MainTabsScreen : Screen {
    @Composable
    override fun Content() {
        val fabSize = 62.dp
        var selectedRoute by remember { mutableStateOf<BottomNavItem>(BottomNavItem.Home) }
        var showAddTransactionDialog by rememberSaveable { mutableStateOf(false) }
        MilkSellerTheme {
            BoxWithConstraints(Modifier.fillMaxSize()) {
                val fabOffset = (maxWidth / 2) - ((fabSize) / 2) - 16.dp
                Scaffold(
                    floatingActionButton = {
                        FloatingActionButton(
                            onClick = { showAddTransactionDialog = true },
                            shape = CircleShape,
                            containerColor = MaterialTheme.colorScheme.secondary,
                            contentColor = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(end = fabOffset, bottom = 34.dp)
                                .size(fabSize)
                        ) {
                            Image(
                                modifier = Modifier.size(32.dp),
                                painter = painterResource(Res.drawable.add_icon),
                                contentDescription = "Add",
                                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface),
                            )
                        }
                    },
                    floatingActionButtonPosition = FabPosition.EndOverlay,
                    bottomBar = {
                        BottomNavBar(selectedRoute = selectedRoute) {
                            selectedRoute = it
                        }
                    }
                ) {
                    Surface(
                        shape = roundCorner(),
                        modifier = Modifier.padding(bottom = it.calculateBottomPadding())
                    ) {
                        when (selectedRoute) {
                            BottomNavItem.Home -> HomeLandingScreen().Content()
                            BottomNavItem.Settings -> SettingsLandingScreen().Content()
                            BottomNavItem.Summary -> UsersListScreen().Content()
                            BottomNavItem.Users -> UsersListScreen().Content()
                        }
                    }

                    if (showAddTransactionDialog) {
                        TransactionDialog(onDismiss = { showAddTransactionDialog = false })
                    }
                }
            }
        }
    }
}

