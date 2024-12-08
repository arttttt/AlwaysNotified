package com.arttttt.appslist.impl.components.app

import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import com.arttttt.appslist.impl.components.app.di.appModule
import com.arttttt.appslist.impl.domain.entity.AppInfo
import com.arttttt.appslist.impl.ui.app.AppContent
import com.arttttt.core.arch.content.ComponentContent
import com.arttttt.core.arch.context.AppComponentContext
import com.arttttt.core.arch.dialog.DismissEvent
import com.arttttt.core.arch.dialog.DismissEventConsumer
import com.arttttt.core.arch.dialog.DismissEventDelegate
import com.arttttt.core.arch.dialog.DismissEventProducer
import com.arttttt.core.arch.events.producer.EventsProducerDelegate
import com.arttttt.core.arch.events.producer.EventsProducerDelegateImpl
import com.arttttt.core.arch.koinScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import org.koin.core.component.getScopeId
import org.koin.core.qualifier.qualifier

internal class AppComponentImpl(
    context: AppComponentContext,
    app: AppInfo,
    private val dismissEventDelegate: DismissEventDelegate = DismissEventDelegate(),
) : AppComponent,
    AppComponentContext by context,
    EventsProducerDelegate<AppComponent.Event> by EventsProducerDelegateImpl(),
    DismissEventConsumer by dismissEventDelegate,
    DismissEventProducer by dismissEventDelegate {

    private val koinScope = koinScope(
        appModule,
        scopeID = getScopeId(),
        qualifier = qualifier<AppComponent>()
    )

    private val coroutineScope = coroutineScope()

    private val transformer: AppTransformer = koinScope.get()

    override val content: ComponentContent = AppContent(this)

    private val states = MutableStateFlow(
        AppComponent.State(
            app = app,
            isDirty = false,
        )
    )

    override val uiStates: StateFlow<AppComponent.UIState> = states
        .map(transformer)
        .flowOn(Dispatchers.IO)
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = transformer(states.value),
        )

    override fun onActivityClicked(name: String) {
        states.update { state ->
            state.copy(
                isDirty = true,
            )
        }
    }

    override fun onManualModeChanged() {
        states.update { state ->
            state.copy(
                isDirty = true,
            )
        }
    }

    override fun onDismiss(event: DismissEvent) {
        if (states.value.isDirty) {
            dispatch(
                AppComponent.Event.EditingFinished(
                    pkg = states.value.app.pkg,
                )
            )
        }

        dismissEventDelegate.onDismiss(event)
    }

    override fun onLaunchClicked() {
    }
}