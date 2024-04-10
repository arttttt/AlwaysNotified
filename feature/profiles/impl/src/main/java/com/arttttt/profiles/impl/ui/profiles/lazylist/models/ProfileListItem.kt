package com.arttttt.profiles.impl.ui.profiles.lazylist.models

import com.arttttt.lazylist.ListItem

internal data class ProfileListItem(
    val id: String,
    val title: String,
    val color: Int,
    val isSelected: Boolean,
    override val key: Any = id,
) : ListItem