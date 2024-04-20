package com.arttttt.appslist.impl.ui.app.lazylist.models

import com.arttttt.lazylist.ListItem

internal data class ActivityListItem(
    val pkg: String,
    val title: String,
    val name: String,
    val isSelected: Boolean,
    override val key: Any
) : ListItem