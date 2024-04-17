package com.arttttt.topbar.impl.components.actions

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.arttttt.appssearch.api.AppsSearchComponent
import com.arttttt.profiles.api.ProfilesComponent

internal sealed interface TopBarAction {

    val icon: ImageVector

    data class Profiles(
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

    data object Settings : TopBarAction {

        override val icon: ImageVector = Icons.Default.Settings
    }

    data class AppsSearch(
        private val component: AppsSearchComponent,
    ) : TopBarAction,
        ExpandableTopBarAction {

        override val icon: ImageVector = Icons.Default.Search

        @Composable
        override fun Content() {
            component.content.Content(
                modifier = Modifier,
            )
        }
    }
}