package org.example.mandm

import androidx.compose.material3.ButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.example.mandm.theme.AppColors


object ColorsUtils {
    @Composable
    fun getButtonColors() = ButtonColors(
        containerColor = MaterialTheme.colorScheme.secondary,
        contentColor = MaterialTheme.colorScheme.onPrimary,
        disabledContainerColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f),
        disabledContentColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.5f)
    )

    @Composable
    fun getInputFieldColors() =
        OutlinedTextFieldDefaults.colors(
            focusedBorderColor = AppColors.Transparent,
            unfocusedBorderColor = AppColors.Transparent,
            errorBorderColor = MaterialTheme.colorScheme.error,
            disabledBorderColor = AppColors.LineGrey.copy(alpha = 0.4f),

            // Background
            focusedContainerColor = AppColors.InputBackground,
            unfocusedContainerColor = AppColors.InputBackground,
            errorContainerColor = AppColors.InputBackground,
            disabledContainerColor = AppColors.InputBackground,

            // Text + hint colors
            focusedTextColor = MaterialTheme.colorScheme.onSurface,
            unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
            disabledTextColor = AppColors.LineGrey.copy(alpha = 0.6f),
            errorTextColor = MaterialTheme.colorScheme.onSurface,

            focusedPlaceholderColor = MaterialTheme.colorScheme.onSurface,
            unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSurface,
            disabledPlaceholderColor = AppColors.LineGrey.copy(alpha = 0.6f),
            errorPlaceholderColor = MaterialTheme.colorScheme.onSurface
        )
}

