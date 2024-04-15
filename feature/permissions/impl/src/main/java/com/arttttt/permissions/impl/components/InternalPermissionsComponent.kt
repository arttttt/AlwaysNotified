package com.arttttt.permissions.impl.components

import com.arkivanov.decompose.value.Value
import com.arttttt.lazylist.ListItem
import com.arttttt.permissions.impl.domain.entity.Permission2
import kotlin.reflect.KClass

internal interface InternalPermissionsComponent {

    data class State(
        val items: List<ListItem>,
    )

    val state: Value<State>

    fun grantPermissionClicked(permission: KClass<out Permission2>)
}