package com.arttttt.appholder.ui.profiles.lazylist.models

import com.arttttt.appholder.ui.base.ListItem

data class ProfileListItem(
    val id: String,
    val title: String,
    val color: Int,
    val isSelected: Boolean,
    override val key: Any = id,
) : ListItem