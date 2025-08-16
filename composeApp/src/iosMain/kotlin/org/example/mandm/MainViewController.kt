package org.example.mandm

import androidx.compose.ui.window.ComposeUIViewController
import org.example.mandm.di.initKoin

fun MainViewController() = ComposeUIViewController(configure = { initKoin() }) { App() }