package com.arttttt.topbar.impl.components.actions

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arttttt.profiles.api.ProfilesComponent

data class ProfilesTopBarAction(
    private val component: ProfilesComponent
) : TopBarAction,
    ExpandableTopBarAction {

    @Composable
    override fun Icon(
        onClick: () -> Unit,
    ) {
        IconButton(
            onClick = onClick,
        ) {
            androidx.compose.material3.Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = null
            )
        }
    }

    @Composable
    override fun ExpandedContent() {
        component.content.Content(
            modifier = Modifier,
        )
    }
}