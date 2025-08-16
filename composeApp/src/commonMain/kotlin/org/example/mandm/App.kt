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
import org.example.mandm.screens.DailyData
import org.example.mandm.screens.SplashScreen
import org.example.mandm.viewModels.UserViewModel
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
@Preview
fun App(
    viewModel: UserViewModel = koinViewModel()
) {
    val fabSize = 62.dp

    var showSplash by rememberSaveable { mutableStateOf(true) }
    var showAddTransactionDialog by rememberSaveable { mutableStateOf(true) }

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
                        BottomNavBar(selectedRoute = selectedRoute) {
                            selectedRoute = it
                        }
                    }) {
                    Surface(
                        shape = roundCorner(),
                        modifier = Modifier.padding(bottom = it.calculateBottomPadding())
                    ) {
                        when (selectedRoute) {
                            BottomNavItem.Home -> {
                                DailyData()
                            }

                            BottomNavItem.Settings -> {}
                            BottomNavItem.Summary -> {}
                            BottomNavItem.Users -> {}
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


