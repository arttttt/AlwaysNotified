package com.arttttt.topbar.impl.components.actions

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable

data object SettingsTopBarAction : TopBarAction {

    @Composable
    override fun Icon(
        onClick: () -> Unit,
    ) {
        IconButton(
            onClick = onClick,
        ) {
            androidx.compose.material3.Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = null
            )
        }
    }
}