package com.arttttt.appssearch.impl.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.arttttt.appssearch.impl.components.InternalAppsSearchComponent
import com.arttttt.core.arch.content.ComponentContent

internal class AppsSearchComponentContentImpl(
    private val component: InternalAppsSearchComponent,
) : ComponentContent {

    @Composable
    override fun Content(modifier: Modifier) {
        val uiState by component.uiState.collectAsState()

        SearchInput(
            text = uiState.text,
            onTextChanged = component::onTextChanged,
        )
    }
}