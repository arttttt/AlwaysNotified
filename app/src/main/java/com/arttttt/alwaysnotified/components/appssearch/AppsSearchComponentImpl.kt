package com.arttttt.alwaysnotified.components.appssearch

import com.arttttt.alwaysnotified.arch.shared.context.AppComponentContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class AppsSearchComponentImpl(
    context: AppComponentContext,
) : AppsSearchComponent,
    AppComponentContext by context {

        private val _uiState = MutableStateFlow(
            AppsSearchComponent.UiState(
                text = "",
            )
        )

    override val uiState: StateFlow<AppsSearchComponent.UiState> = _uiState.asStateFlow()

    override fun onTextChanged(text: String) {
        _uiState.update { uiState ->
            uiState.copy(
                text = text,
            )
        }
    }
}