package org.example.mandm.commonComponent

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.example.mandm.roundCorneButton
import org.example.mandm.theme.AppColors

@Composable
fun SelectableOptionWithCheckbox(
    modifier: Modifier = Modifier,
    title: String,
    selected: Boolean,
    checked: Boolean,
    onClick: () -> Unit,
    onCheckedChange: (Boolean) -> Unit
) {
    val borderColor = if (selected) AppColors.Green else AppColors.LineGrey
    val backgroundColor = if (selected) AppColors.Green.copy(alpha = 0.12f) else AppColors.InputBackground

    Box(
        modifier = modifier
            .clickable { onClick() }
            .background(backgroundColor, shape = roundCorneButton())
            .border(1.5.dp, borderColor, shape = roundCorneButton())
            .padding(horizontal = 12.dp, vertical = 16.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Checkbox(
                checked = checked,
                onCheckedChange = { onCheckedChange(it) },
                colors = CheckboxDefaults.colors(
                    checkedColor = MaterialTheme.colorScheme.secondary,
                    uncheckedColor = MaterialTheme.colorScheme.primary,
                    checkmarkColor = Color.White
                )
            )
            Text(
                text = title,
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}


