package com.arttttt.core.arch.content

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

fun interface ComponentContent {

    @Composable
    fun Content(
        modifier: Modifier,
    )
}