package com.arttttt.appssearch.impl.components

import kotlinx.coroutines.flow.StateFlow

internal interface InternalAppsSearchComponent {

    data class UiState(
        val text: String,
        val selectedAppsOnly: Boolean,
    )

    val uiState: StateFlow<UiState>

    fun onTextChanged(text: String)
    fun onSelectedAppsOnlyToggled()
}