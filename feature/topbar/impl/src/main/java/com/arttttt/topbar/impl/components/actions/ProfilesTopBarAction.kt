package com.arttttt.topbar.impl.components.actions

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.arttttt.profiles.api.ProfilesComponent

data class ProfilesTopBarAction(
    private val component: ProfilesComponent
) : TopBarAction,
    ExpandableTopBarAction {

    override val icon: ImageVector = Icons.Default.KeyboardArrowDown

    @Composable
    override fun Content() {
        component.content.Content(
            modifier = Modifier,
        )
    }
}