package com.arttttt.topbar.impl.components.actions

import androidx.compose.runtime.Composable

internal sealed interface TopBarAction {

    @Composable
    fun Icon(
        onClick: () -> Unit,
    )
}