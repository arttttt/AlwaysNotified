package com.arttttt.appssearch.impl.components

import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import com.arttttt.appssearch.api.AppsSearchComponent
import com.arttttt.appssearch.impl.ui.AppsSearchComponentContentImpl
import com.arttttt.core.arch.content.ComponentContent
import com.arttttt.core.arch.context.AppComponentContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

internal class AppsSearchComponentImpl(
    context: AppComponentContext,
) : AppsSearchComponent,
    InternalAppsSearchComponent,
    AppComponentContext by context {

    private val coroutinesScope = coroutineScope()

    private val _states = MutableStateFlow(
        AppsSearchComponent.State(
            filter = null,
            selectedAppsOnly = false,
        )
    )

    override val content: ComponentContent = AppsSearchComponentContentImpl(this)

    override val states: StateFlow<AppsSearchComponent.State> = _states

    override val uiState: StateFlow<InternalAppsSearchComponent.UiState> = _states
        .map { state ->
            InternalAppsSearchComponent.UiState(
                text = state.filter ?: "",
                selectedAppsOnly = state.selectedAppsOnly,
            )
        }
        .stateIn(
            coroutinesScope,
            SharingStarted.WhileSubscribed(5000),
            InternalAppsSearchComponent.UiState(
                text = "",
                selectedAppsOnly = false,
            ),
        )

    override fun onTextChanged(text: String) {
        _states.update { state ->
            state.copy(
                filter = text.takeIf { it.isNotEmpty() },
            )
        }
    }

    override fun onSelectedAppsOnlyToggled() {
        _states.update { state ->
            state.copy(
                selectedAppsOnly = !state.selectedAppsOnly,
            )
        }
    }
}