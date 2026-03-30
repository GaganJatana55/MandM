package org.example.mandm

import MilkSellerTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import org.example.mandm.screens.AuthNavigation
import org.example.mandm.screens.MainTabsScreen
import org.example.mandm.screens.SplashScreen
import org.jetbrains.compose.ui.tooling.preview.Preview


@Preview
@Composable
fun App() {
    var showSplash by rememberSaveable { mutableStateOf(true) }
    // Reactive state for login
    var isLoggedIn by remember { mutableStateOf(AppPreferences.isUserLoggedIn()) }

    MilkSellerTheme {
        if (showSplash) {
            SplashScreen(onNavigateToMain = { showSplash = false })
        } else {
            // Conditional flow selection
            if (!isLoggedIn) {
                // AUTH FLOW
                AuthNavigation(onLoginSuccess = {
                    AppPreferences.setIsUserLoggedIn(true)
                    isLoggedIn = true // This triggers the else block below
                })
            } else {
                // MAIN APP FLOW
                MainTabsScreen()
            }
        }
    }
}