package com.arttttt.alwaysnotified.components.appssearch

import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import com.arttttt.core.arch.context.AppComponentContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class AppsSearchComponentImpl(
    context: com.arttttt.core.arch.context.AppComponentContext,
) : AppsSearchComponent,
    com.arttttt.core.arch.context.AppComponentContext by context {

    private val coroutinesScope = coroutineScope()

    private val _states = MutableStateFlow(
        AppsSearchComponent.State(
            filter = "",
        )
    )

    override val states: StateFlow<AppsSearchComponent.State> = _states

    override val uiState: StateFlow<AppsSearchComponent.UiState> = _states
        .map { state ->
            AppsSearchComponent.UiState(
                text = state.filter,
            )
        }
        .stateIn(
            coroutinesScope,
            SharingStarted.WhileSubscribed(5000),
            AppsSearchComponent.UiState(
                text = "",
            ),
        )

    override fun onTextChanged(text: String) {
        _states.update { state ->
            state.copy(
                filter = text,
            )
        }
    }
}