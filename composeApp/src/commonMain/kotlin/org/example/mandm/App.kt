package org.example.mandm

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
import mandm.composeapp.generated.resources.Res
import mandm.composeapp.generated.resources.add_icon
import org.example.mandm.screens.TransactionDialog
import org.example.mandm.screens.DailyRouteData
import org.example.mandm.screens.SelectRole
import org.example.mandm.screens.SignInUI
import org.example.mandm.screens.SignUpUI
import org.example.mandm.screens.SplashScreen
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import org.example.mandm.NavigationConfig
import org.example.mandm.TabResetPolicy
import org.example.mandm.screens.HomeTab
import org.example.mandm.screens.SettingsTab
import org.example.mandm.screens.SummaryTab
import org.example.mandm.screens.UsersTab
import org.example.mandm.screens.SelectRoleScreen
import org.example.mandm.viewModels.UserViewModel
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import org.example.mandm.AppPreferences

@Composable
@Preview
fun App(
    viewModel: UserViewModel = koinViewModel()
) {
    val fabSize = 62.dp

    var showSplash by rememberSaveable { mutableStateOf(true) }
    var showAddTransactionDialog by rememberSaveable { mutableStateOf(true) }
    var isLoggedIn by rememberSaveable { mutableStateOf(AppPreferences.isUserLoggedIn()) }

    var selectedRoute by remember { mutableStateOf<BottomNavItem>(BottomNavItem.Home) }
    MilkSellerTheme {
        if (showSplash) {
            SplashScreen { showSplash = false }
        } else {
            BoxWithConstraints(Modifier.fillMaxSize()) {
                val fabOffset = (maxWidth / 2) - ((fabSize) / 2) - 16.dp
                Scaffold(

                    floatingActionButton = {
                        FloatingActionButton(
                            onClick = {
                                showAddTransactionDialog = true
                            },
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
                        if (isLoggedIn) {
                            BottomNavBar(selectedRoute = selectedRoute) {
                                selectedRoute = it
                            }
                        }
                    }) {
                    val isLoggedInLocal = isLoggedIn
                    Surface(
                        shape = roundCorner(),
                        modifier = Modifier.padding(bottom = it.calculateBottomPadding())
                    ) {
                        if (!isLoggedInLocal) {
                            Navigator(SelectRoleScreen(onSignUpComplete = { isLoggedIn = true }))
                        } else {
                            TabNavigator(HomeTab) { tabNavigator ->
                                when (NavigationConfig.currentTabResetPolicy) {
                                    TabResetPolicy.ResetOnEnter, TabResetPolicy.Both -> {
                                        when (selectedRoute) {
                                            BottomNavItem.Home -> HomeTab.resetNextEnter()
                                            BottomNavItem.Settings -> SettingsTab.resetNextEnter()
                                            BottomNavItem.Summary -> SummaryTab.resetNextEnter()
                                            BottomNavItem.Users -> UsersTab.resetNextEnter()
                                        }
                                    }
                                    else -> {}
                                }

                                tabNavigator.current = when (selectedRoute) {
                                    BottomNavItem.Home -> HomeTab
                                    BottomNavItem.Settings -> SettingsTab
                                    BottomNavItem.Summary -> SummaryTab
                                    BottomNavItem.Users -> UsersTab
                                }

                                CurrentTab()
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
}


