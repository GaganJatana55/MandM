package org.example.mandm.commonComponent

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.example.mandm.roundCorneButton
import org.example.mandm.theme.AppColors

@Composable
 fun RoleOption(
    modifier: Modifier = Modifier,
    title: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    val borderColor = if (selected) AppColors.Green else AppColors.LineGrey
    val backgroundColor = if (selected) AppColors.Green.copy(alpha = 0.12f) else AppColors.InputBackground

    Box(
        modifier = modifier
            .padding(horizontal = 8.dp)
            .clickable { onClick() }
            .background(backgroundColor, shape = roundCorneButton())
            .border(1.5.dp, borderColor, shape = roundCorneButton())
            .padding(vertical = 24.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title,
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold
        )
    }
}
