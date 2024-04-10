package com.arttttt.appssearch.api

import com.arttttt.core.arch.DecomposeComponent
import com.arttttt.core.arch.content.ComponentContentOwner
import com.arttttt.core.arch.context.AppComponentContext
import kotlinx.coroutines.flow.StateFlow

interface AppsSearchComponent : DecomposeComponent, ComponentContentOwner {

    fun interface Factory {

        fun create(context: AppComponentContext): AppsSearchComponent
    }

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