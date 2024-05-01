package com.arttttt.topbar.impl.components.actions

import androidx.compose.ui.graphics.vector.ImageVector

internal sealed interface TopBarAction {

    val icon: ImageVector
}