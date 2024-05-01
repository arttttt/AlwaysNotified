package com.arttttt.appssearch.impl.components

import kotlinx.coroutines.flow.StateFlow

internal interface InternalAppsSearchComponent {

    data class UiState(
        val text: String
    )

    val uiState: StateFlow<UiState>

    fun onTextChanged(text: String)
}