package com.arttttt.appholder.ui.appslist.lazylist.models

import com.arttttt.appholder.ui.base.ListItem

data class ActivityListItem(
    val pkg: String,
    val title: String,
    val name: String,
    val isSelected: Boolean,
    override val key: Any
) : ListItem