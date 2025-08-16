package org.example.mandm

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

fun Modifier.paddingTopCommon(): Modifier {
    return this.padding(top = 12.dp)
}

fun Modifier.paddingCommon(): Modifier {
    return this.padding(12.dp)
}

fun Modifier.paddingParentCommon(): Modifier {
    return this.padding(horizontal = 12.dp)
}

@Composable
fun Modifier.backgroundCommonCard(): Modifier {
    return this.background(MaterialTheme.colorScheme.surface)
}

@Composable
fun Modifier.mainBackground(): Modifier {
    return this.background(MaterialTheme.colorScheme.background)
}

fun roundCorner(): RoundedCornerShape {
    return RoundedCornerShape(CornerSize(24.dp))
}

fun roundCornerTop(): RoundedCornerShape {
    return RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
}

fun roundCornerBottom(): RoundedCornerShape {
    return RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)
}

@Composable
fun Modifier.commonBorder(): Modifier {
    return border(
        1.5.dp,
        Color.LightGray,
        RoundedCornerShape(12.dp)
    )
}