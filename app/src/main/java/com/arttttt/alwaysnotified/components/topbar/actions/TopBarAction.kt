package com.arttttt.alwaysnotified.components.topbar.actions

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import com.arttttt.alwaysnotified.components.profiles.ProfilesComponent
import com.arttttt.alwaysnotified.ui.profiles.ProfilesContent

sealed class TopBarAction {

    abstract val icon: ImageVector

    data class Profiles(
        private val component: ProfilesComponent
    ) : TopBarAction(), ExpandableTopBarAction {

        override val icon: ImageVector = Icons.Default.KeyboardArrowDown

        @Composable
        override fun Content() {
            ProfilesContent(component)
        }
    }

    data object Settings : TopBarAction() {

        override val icon: ImageVector = Icons.Default.Settings
    }
}