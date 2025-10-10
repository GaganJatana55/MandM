package org.example.mandm.commonComponent

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.example.mandm.roundCorneButton
import org.example.mandm.theme.AppColors

@Composable
fun OutlineActionButton(
    modifier: Modifier = Modifier,
    text: String,
    borderColor: Color = MaterialTheme.colorScheme.error,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    val appliedAlpha = if (enabled) 1f else 0.5f
    val color = borderColor.copy(alpha = appliedAlpha)

    Box(
        modifier = modifier
            .border(width = 1.5.dp, color = color, shape = roundCorneButton())
            .clickable(enabled = enabled) { onClick() }
            .padding(horizontal = 16.dp, vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = color,
            style = MaterialTheme.typography.labelMedium,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
fun FilledActionButton(
    modifier: Modifier = Modifier,
    text: String,
    fillColor: Color = AppColors.Green,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    val appliedAlpha = if (enabled) 1f else 0.5f
    val color = fillColor.copy(alpha = appliedAlpha)

    Box(
        modifier = modifier
            .background(color = color, shape = roundCorneButton())
            .clickable(enabled = enabled) { onClick() }
            .padding(horizontal = 16.dp, vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = AppColors.White,
            style = MaterialTheme.typography.labelMedium,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}



