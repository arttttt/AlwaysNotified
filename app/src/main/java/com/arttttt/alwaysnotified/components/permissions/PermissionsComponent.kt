package com.arttttt.alwaysnotified.components.permissions

import com.arkivanov.decompose.value.Value
import com.arttttt.alwaysnotified.arch.shared.DecomposeComponent
import com.arttttt.alwaysnotified.arch.shared.context.AppComponentContext
import com.arttttt.alwaysnotified.arch.shared.events.producer.EventsProducer
import com.arttttt.alwaysnotified.domain.entity.permission.Permission2
import com.arttttt.alwaysnotified.ui.base.ListItem
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