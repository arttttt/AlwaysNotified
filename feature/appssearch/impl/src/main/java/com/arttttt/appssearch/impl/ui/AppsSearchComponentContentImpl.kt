package com.arttttt.appssearch.impl.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.arttttt.appssearch.impl.components.InternalAppsSearchComponent
import com.arttttt.core.arch.content.ComponentContent

internal class AppsSearchComponentContentImpl(
    private val component: InternalAppsSearchComponent,
) : ComponentContent {

    @Composable
    override fun Content(modifier: Modifier) {
        val uiState by component.uiState.collectAsState()

        Column(
            modifier = modifier
        ) {
            SearchInput(
                text = uiState.text,
                onTextChanged = component::onTextChanged,
            )

            FilterChip(
                selected = uiState.selectedAppsOnly,
                onClick = component::onSelectedAppsOnlyToggled,
                label = {
                    Text(
                        text = stringResource(id = com.arttttt.localization.R.string.selected_apps),
                    )
                }
            )
        }
    }
}