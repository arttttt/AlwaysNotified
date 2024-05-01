package com.arttttt.topbar.impl.components.actions

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arttttt.appssearch.api.AppsSearchComponent

data class AppsSearchTopBarAction(
    private val component: AppsSearchComponent,
) : TopBarAction,
    ExpandableTopBarAction {

    @Composable
    override fun Icon(
        onClick: () -> Unit,
    ) {
        val state by component.states.collectAsState()

        Box {
            if (state.filter != null) {
                Badge(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(12.dp),
                )
            }

            IconButton(
                onClick = onClick,
            ) {
                androidx.compose.material3.Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null
                )
            }
        }
    }

    @Composable
    override fun ExpandedContent() {
        component.content.Content(
            modifier = Modifier,
        )
    }
}