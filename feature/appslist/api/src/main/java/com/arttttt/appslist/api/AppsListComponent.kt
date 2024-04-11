package com.arttttt.appslist.api

import com.arttttt.core.arch.DecomposeComponent
import com.arttttt.core.arch.content.ComponentContentOwner
import com.arttttt.core.arch.context.AppComponentContext
import com.arttttt.core.arch.events.producer.EventsProducer

interface AppsListComponent : DecomposeComponent,
    ComponentContentOwner,
    EventsProducer<AppsListComponent.Event> {

    fun interface Factory {

        fun create(context: AppComponentContext): AppsListComponent
    }

    sealed class Event {
        data object OpenSettings : Event()
    }
}