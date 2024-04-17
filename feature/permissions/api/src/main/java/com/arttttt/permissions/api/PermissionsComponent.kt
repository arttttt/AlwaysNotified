package com.arttttt.permissions.api

import com.arttttt.core.arch.DecomposeComponent
import com.arttttt.core.arch.content.ComponentContentOwner
import com.arttttt.core.arch.context.AppComponentContext
import com.arttttt.core.arch.events.producer.EventsProducer

interface PermissionsComponent :
    DecomposeComponent,
    ComponentContentOwner,
    EventsProducer<PermissionsComponent.Event> {

    fun interface Factory {

        fun create(context: AppComponentContext): PermissionsComponent
    }

    sealed class Event {

        data object AllPermissionsGranted : Event()
    }

    fun needRequestPermissions(): Boolean
}