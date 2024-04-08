package com.arttttt.alwaysnotified.components.appssearch

import com.arttttt.alwaysnotified.arch.shared.DecomposeComponent
import kotlinx.coroutines.flow.StateFlow

interface AppsSearchComponent : DecomposeComponent {

    data class UiState(
        val text: String
    )

    data class State(
        val filter: String
    )

    val uiState: StateFlow<UiState>

    val states: StateFlow<State>

    fun onTextChanged(text: String)
}