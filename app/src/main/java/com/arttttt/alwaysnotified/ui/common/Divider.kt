package com.arttttt.alwaysnotified.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arttttt.alwaysnotified.ui.theme.AppTheme

@Composable
fun Divider(modifier: Modifier) {
    Spacer(
        modifier = modifier
            .height(1.dp)
            .background(AppTheme.colors.divider)
    )
}