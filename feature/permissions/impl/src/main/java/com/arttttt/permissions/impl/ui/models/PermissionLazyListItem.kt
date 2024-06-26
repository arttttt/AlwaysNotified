package com.arttttt.permissions.impl.ui.models

import com.arttttt.permissions.impl.domain.entity.Permission2
import com.arttttt.lazylist.ListItem
import kotlin.reflect.KClass

internal sealed class PermissionLazyListItem : ListItem {

    abstract val title: String

    data class Granted(
        override val key: Any,
        override val title: String
    ) : PermissionLazyListItem()

    data class Denied(
        override val title: String,
        val permission: KClass<out Permission2>,
    ) : PermissionLazyListItem() {

        override val key: Any = permission
    }
}