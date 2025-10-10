package org.example.mandm.commonComponent

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import mandm.composeapp.generated.resources.Res
import mandm.composeapp.generated.resources.left_icon
import mandm.composeapp.generated.resources.right_icon
import org.jetbrains.compose.resources.painterResource
import androidx.compose.foundation.Image

@Composable
fun MiniPager(
    modifier: Modifier = Modifier,
    currentIndex: Int,
    totalCount: Int,
    prevLabel: String? = null,
    nextLabel: String? = null,
    onPrev: () -> Unit,
    onNext: () -> Unit
) {
    Row(
        modifier = modifier.padding(horizontal = 10.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Left arrow and optional preview label
        Row(verticalAlignment = Alignment.CenterVertically) {
            val prevEnabled = currentIndex > 0
            Surface(
                shape = CircleShape,
                shadowElevation = 2.dp,
                color = MaterialTheme.colorScheme.surface,
            ) {
                Box(
                    modifier = Modifier.size(32.dp)
                        .clickable(enabled = prevEnabled) { onPrev() },
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(Res.drawable.left_icon),
                        contentDescription = "Previous"
                    )
                }
            }
            if (prevLabel != null) {
                Spacer(Modifier.size(6.dp))
                Text(
                    text = prevLabel,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }

        // Dots
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.CenterVertically) {
            val safeTotal = totalCount.coerceAtLeast(1)
            repeat(safeTotal.coerceAtMost(9)) { index ->
                val isSelected = index == (currentIndex.coerceIn(0, safeTotal - 1))
                Box(
                    modifier = Modifier
                        .size(if (isSelected) 8.dp else 6.dp)
                        .background(
                            color = if (isSelected) MaterialTheme.colorScheme.onSurface else Color.LightGray,
                            shape = CircleShape
                        )
                )
            }
        }

        // Right arrow and optional preview label
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (nextLabel != null) {
                Text(
                    text = nextLabel,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    style = MaterialTheme.typography.labelSmall
                )
                Spacer(Modifier.size(6.dp))
            }
            val nextEnabled = currentIndex < totalCount - 1
            Surface(
                shape = CircleShape,
                shadowElevation = 2.dp,
                color = MaterialTheme.colorScheme.surface,
            ) {
                Box(
                    modifier = Modifier.size(32.dp)
                        .clickable(enabled = nextEnabled) { onNext() },
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(Res.drawable.right_icon),
                        contentDescription = "Next"
                    )
                }
            }
        }
    }
}



