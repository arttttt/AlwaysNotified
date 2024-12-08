package com.arttttt.appslist.impl.ui.app.lazylist.models

import com.arttttt.lazylist.ListItem

internal data class ComponentListItem(
    val pkg: String,
    val title: String,
    val name: String,
) : ListItem {
    override val key: Any = "${pkg}_$name"
}