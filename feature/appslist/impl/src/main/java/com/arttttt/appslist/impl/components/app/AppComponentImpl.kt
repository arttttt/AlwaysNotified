package com.arttttt.appslist.impl.components.app

import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import com.arttttt.appslist.SelectedActivity
import com.arttttt.appslist.impl.domain.entity.AppInfo
import com.arttttt.appslist.impl.ui.app.AppContent
import com.arttttt.core.arch.content.ComponentContent
import com.arttttt.core.arch.context.AppComponentContext
import com.arttttt.core.arch.dialog.DismissEventConsumer
import com.arttttt.core.arch.dialog.DismissEventDelegate
import com.arttttt.core.arch.dialog.DismissEventProducer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

internal class AppComponentImpl(
    context: AppComponentContext,
    app: AppInfo,
    selectedActivity: SelectedActivity?,
    dismissEventDelegate: DismissEventDelegate = DismissEventDelegate(),
) : AppComponent,
    AppComponentContext by context,
    DismissEventConsumer by dismissEventDelegate,
    DismissEventProducer by dismissEventDelegate {

    private val coroutinesScope = coroutineScope()

    private val transformer = AppTransformer()

    override val content: ComponentContent = AppContent(this)

    private val states = MutableStateFlow(
        AppComponent.State(
            app = app,
            selectedActivity = selectedActivity,
        )
    )

    override val uiStates: StateFlow<AppComponent.UIState> = states
        .map(transformer)
        .stateIn(
            scope = coroutinesScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = transformer(states.value),
        )

    override fun onActivityClicked(name: String) {
    }
}