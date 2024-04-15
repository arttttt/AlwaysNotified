package com.arttttt.permissions.api

import com.arkivanov.decompose.value.Value
import com.arttttt.lazylist.ListItem
import com.arttttt.core.arch.DecomposeComponent
import com.arttttt.core.arch.content.ComponentContentOwner
import com.arttttt.core.arch.context.AppComponentContext
import com.arttttt.core.arch.events.producer.EventsProducer
import kotlin.reflect.KClass

interface PermissionsComponent :
    DecomposeComponent,
    ComponentContentOwner,
    EventsProducer<PermissionsComponent.Event> {

    fun interface Factory {

        fun create(context: AppComponentContext): PermissionsComponent
    }

    data class State(
        val items: List<ListItem>,
    )

    sealed class Event {

        data object AllPermissionsGranted : Event()
    }

    val state: Value<State>

    fun needRequestPermissions(): Boolean

    fun grantPermissionClicked(permission: KClass<out Permission2>)
}