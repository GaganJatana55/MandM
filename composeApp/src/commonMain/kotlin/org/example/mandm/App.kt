package org.example.mandm

import BottomNavItem
import MilkSellerTheme
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.Navigator
import org.example.mandm.screens.MainTabsScreen
import org.example.mandm.screens.SelectRoleScreen
import org.example.mandm.screens.SplashScreen
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App(

) {


    var showSplash by rememberSaveable { mutableStateOf(true) }

    var isLoggedIn by rememberSaveable { mutableStateOf(AppPreferences.isUserLoggedIn()) }



    MilkSellerTheme {
        if (showSplash) {
            SplashScreen { showSplash = false }
        } else {
            BoxWithConstraints(Modifier.fillMaxSize()) {
                val isLoggedInLocal = isLoggedIn
                if (!isLoggedInLocal) {
                    Navigator(SelectRoleScreen(onSignUpComplete = {
                        AppPreferences.setIsUserLoggedIn(true)
                        isLoggedIn = true
                    }))
                } else {
                    Navigator(MainTabsScreen())
                }
            }
        }
    }
}


