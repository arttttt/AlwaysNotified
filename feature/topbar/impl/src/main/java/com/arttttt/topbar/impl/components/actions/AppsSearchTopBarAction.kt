package com.arttttt.topbar.impl.components.actions

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.arttttt.appssearch.api.AppsSearchComponent

data class AppsSearchTopBarAction(
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