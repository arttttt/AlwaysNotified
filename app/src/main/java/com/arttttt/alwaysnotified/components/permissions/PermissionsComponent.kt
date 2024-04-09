package com.arttttt.alwaysnotified.components.permissions

import com.arkivanov.decompose.value.Value
import com.arttttt.alwaysnotified.domain.entity.permission.Permission2
import com.arttttt.core.arch.base.ListItem
import com.arttttt.core.arch.DecomposeComponent
import com.arttttt.core.arch.events.producer.EventsProducer
import kotlin.reflect.KClass

interface PermissionsComponent :
    DecomposeComponent,
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