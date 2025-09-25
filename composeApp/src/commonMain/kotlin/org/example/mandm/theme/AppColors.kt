package org.example.mandm.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

object AppColors {

    // Green
    private val GreenLight = Color(0xFF388E3C)
    private val GreenDark = Color(0xFF81C784)

    val Green: Color
        @Composable get() = if (isSystemInDarkTheme()) GreenDark else GreenLight


    // Red
    private val RedLight = Color(0xFFFF2900)
    private val RedDark = Color(0xFFFF2900)

    val Red: Color
        @Composable get() = if (isSystemInDarkTheme()) RedDark else RedLight


    // Orange
    private val OrangeLight = Color(0xFFEE7722)
    private val OrangeDark = Color(0xFFEE7722)

    val Orange: Color
        @Composable get() = if (isSystemInDarkTheme()) OrangeDark else OrangeLight


    // Blue
    private val BlueLight = Color(0xFF1976D2)
    private val BlueDark = Color(0xFF64B5F6)

    val Blue: Color
        @Composable get() = if (isSystemInDarkTheme()) BlueDark else BlueLight


    // Grey
    private val GreyLight = Color(0xFF757575)
    private val GreyDark = Color(0xFFBDBDBD)

    val Grey: Color
        @Composable get() = if (isSystemInDarkTheme()) GreyDark else GreyLight


    // Line Grey (for dividers/borders)
    private val HintLight = Color(0x66DDDDDD)
    private val HintDark = Color(0xFF444444)

    val hintText: Color
        @Composable get() = if (isSystemInDarkTheme()) LineGreyDark else LineGreyLight

    // Line Grey (for dividers/borders)
    private val LineGreyLight = Color(0xFFDDDDDD)
    private val LineGreyDark = Color(0xFF444444)

    val LineGrey: Color
        @Composable get() = if (isSystemInDarkTheme()) LineGreyDark else LineGreyLight

    // Text Colors
    val Transparent: Color
        @Composable get() = if (isSystemInDarkTheme()) Color(0x33FFFFFF) else Color(0x33000000)


    // Text Colors
    val TextBlack: Color
        @Composable get() = if (isSystemInDarkTheme()) Color(0xFFFFFFFF) else Color(0xFF000000)

    val TextWhite: Color
        @Composable get() = if (isSystemInDarkTheme()) Color(0xFF000000) else Color(0xFFFFFFFF)


    // --- Absolute Colors (never change) ---
    val Black: Color = Color(0xFF000000)
    val White: Color = Color(0xFFFFFFFF)


    val InputBackground: Color
        @Composable get()=if (isSystemInDarkTheme()) Color(0xFF3D3F41) else Color(0xFFFFFFFF)

    val InputBorder: Color
        @Composable get()=if (isSystemInDarkTheme()) Color(0xFFFFFFFF) else Color(0xFFD1D5DC)
}
