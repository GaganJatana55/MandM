package org.example.mandm.screens

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow

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


