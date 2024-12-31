package com.arttttt.appslist.api

import com.arttttt.core.arch.DecomposeComponent
import com.arttttt.core.arch.content.ComponentContentOwner
import com.arttttt.core.arch.context.AppComponentContext
import com.arttttt.core.arch.events.producer.EventsProducer
import kotlinx.coroutines.flow.StateFlow

interface AppsListComponent : DecomposeComponent,
    ComponentContentOwner,
    EventsProducer<AppsListComponent.Event> {

    fun interface Factory {

        fun create(
            context: AppComponentContext,
            startApps: () -> Unit,
        ): AppsListComponent
    }

    data class State(
        val selectedApps: Set<AppInfo>,
        val isLoading: Boolean,
    )

    sealed class Event {
        data object OpenSettings : Event()
    }

    val states: StateFlow<State>
}