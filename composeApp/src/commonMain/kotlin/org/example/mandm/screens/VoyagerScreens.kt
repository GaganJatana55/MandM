package org.example.mandm.screens


import BottomNavBar
import BottomNavItem
import MilkSellerTheme
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import bottomNavItems
import io.ktor.http.encodeURLParameter
import mandm.composeapp.generated.resources.Res
import mandm.composeapp.generated.resources.add_icon
import org.example.mandm.screens.screenNavigationParams.Billing
import org.example.mandm.screens.screenNavigationParams.BillingDetailsParams
import org.example.mandm.screens.screenNavigationParams.Clients
import org.example.mandm.screens.screenNavigationParams.Home
import org.example.mandm.screens.screenNavigationParams.Routes
import org.example.mandm.screens.screenNavigationParams.RoutesEditDetail
import org.example.mandm.screens.screenNavigationParams.SelectRole
import org.example.mandm.screens.screenNavigationParams.SignUp
import org.example.mandm.screens.screenNavigationParams.topLevelRoutes
import org.jetbrains.compose.resources.painterResource
import androidx.navigation.NavDestination.Companion.hasRoute

@Composable
fun AuthNavigation(onLoginSuccess: () -> Unit) {
    val authNavController = rememberNavController()

    NavHost(
        navController = authNavController,
        startDestination = SelectRole // Serialized object
    ) {
        composable<SelectRole> {
            SelectRole(
                onProceedToSignUp = { authNavController.navigate(SignUp()) }
            )
        }

        composable<SignUp> {
            SignUpUI(
                onComplete = { onLoginSuccess() } // Notifies the parent App()
            )
        }
    }
}

@Composable
fun MainTabsScreen() {

    val navController = rememberNavController()

    CompositionLocalProvider(
        LocalNavController provides navController
    ) {

        // 👇 your existing UI
        MainTabsContent()
    }
}

@Composable
fun MainTabsContent() {
    val fabSize = 62.dp
    val navController = LocalNavController.current
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    var showAddTransactionDialog by rememberSaveable { mutableStateOf(false) }
// THE LOGIC: Is the current screen one of our 4 main tabs?
    val shouldShowBottomBar = topLevelRoutes.any { routeClass ->
        currentDestination?.hasRoute(routeClass) == true
    }
    // This helps the BottomBar know which icon to highlight

    MilkSellerTheme {
        BoxWithConstraints(Modifier.fillMaxSize()) {
            val fabOffset = (maxWidth / 2) - ((fabSize) / 2) - 16.dp
            Scaffold(
                contentWindowInsets = WindowInsets(0, 0, 0, 0),
                floatingActionButton = {
                    if (shouldShowBottomBar) {
                        FloatingActionButton(
                            onClick = { showAddTransactionDialog = true },
                            shape = CircleShape,
                            containerColor = MaterialTheme.colorScheme.secondary,
                            contentColor = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(end = fabOffset, bottom = 44.dp)
                                .size(fabSize)
                        ) {
                            Image(
                                modifier = Modifier.size(32.dp),
                                painter = painterResource(Res.drawable.add_icon),
                                contentDescription = "Add",
                                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface),
                            )
                        }
                    }
                },
                floatingActionButtonPosition = FabPosition.EndOverlay,

                bottomBar = {
                    if (shouldShowBottomBar) {
                        BottomNavBar(
                            currentDestination = currentDestination,
                            onItemClick = { destination ->
                                navController.navigate(destination) {
                                    // Standard Tab Navigation Logic
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            ) { padding ->
                NavHost(
                    navController = navController,
                    startDestination = Home,
                    modifier = Modifier.padding(padding)
                ) {
                    composable<Home> { DailyRouteData() }
                    composable<Billing> { UsersLandingScreen() }
                    composable<Clients> { UsersListScreen() }
                    composable<Routes> { RouteListScreen() }
                    composable<RoutesEditDetail> { backStackEntry ->
                        val args = backStackEntry.toRoute<RoutesEditDetail>()
                        EditRouteUserScreen(args.routeId, args.routeName)
                    }

                    // Modern Type-Safe Arguments
                    composable<BillingDetailsParams> { backStackEntry ->
                        val args = backStackEntry.toRoute<BillingDetailsParams>()
                        BillListScreen(userId = args.userId, userName = args.userName)
                    }
                }
            }
            if (showAddTransactionDialog) {
                TransactionDialog(onDismiss = { showAddTransactionDialog = false })
            }
        }
    }
}



