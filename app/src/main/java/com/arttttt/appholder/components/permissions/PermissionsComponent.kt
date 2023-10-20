package com.arttttt.appholder.components.permissions

import com.arkivanov.decompose.value.Value
import com.arttttt.appholder.arch.DecomposeComponent
import com.arttttt.appholder.arch.context.AppComponentContext
import com.arttttt.appholder.arch.events.EventsProducer
import com.arttttt.appholder.domain.entity.permission.Permission2
import com.arttttt.appholder.ui.base.ListItem
import kotlin.reflect.KClass

interface PermissionsComponent :
    DecomposeComponent,
    AppComponentContext,
    EventsProducer<PermissionsComponent.Event> {

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