package com.arttttt.topbar.impl.components.actions

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arttttt.appssearch.api.AppsSearchComponent

data class AppsSearchTopBarAction(
    private val component: AppsSearchComponent,
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
                imageVector = Icons.Default.Search,
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