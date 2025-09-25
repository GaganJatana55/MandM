package org.example.mandm

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.example.mandm.theme.AppColors

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
fun Modifier.getBorderWithColor(color: Color = AppColors.Green): Modifier {
    return this.border(width = 1.dp, color, shape = roundCorneButton())
}
@Composable
fun Modifier.getStatusBorderColor(status: String) = when (status) {
    TransactionStatus.PENDING -> {
        Modifier.getBorderWithColor(AppColors.Orange)
    }

    TransactionStatus.SKIPPED -> {
        Modifier.getBorderWithColor(AppColors.Red)
    }

    TransactionStatus.ADDED -> {
        Modifier.getBorderWithColor(AppColors.Green)
    }

    else -> {
        Modifier.getBorderWithColor(AppColors.Orange)
    }
}

@Composable
fun Modifier.mainBackground(): Modifier {
    return this.background(MaterialTheme.colorScheme.background)
}

fun roundCorner(): RoundedCornerShape {
    return RoundedCornerShape(CornerSize(24.dp))
}

fun roundCorneButton(): RoundedCornerShape {
    return RoundedCornerShape(CornerSize(12.dp))
}

fun roundCornerTop(): RoundedCornerShape {
    return RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
}

fun roundCornerBottom(): RoundedCornerShape {
    return RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)
}
fun roundCornerInput(): RoundedCornerShape {
    return RoundedCornerShape(corner = CornerSize(6.dp))
}

@Composable
fun Modifier.commonBorder(): Modifier {
    return border(
        1.5.dp,
        Color.LightGray,
        RoundedCornerShape(12.dp)
    )
}

fun compactInputPadding(
    start: Int = 12,
    top: Int = 12,
    end: Int = 12,
    bottom: Int = 12
): PaddingValues {
    return PaddingValues(
        start = start.dp,
        top = top.dp,
        end = end.dp,
        bottom = bottom.dp
    )
}
