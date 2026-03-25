package org.example.mandm.commonComponent

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.example.mandm.theme.AppColors

@Composable
fun Line(modifier: Modifier= Modifier){
    Box(modifier.fillMaxWidth().height(2.dp).background(AppColors.LineGrey))
}