package com.arttttt.profiles.impl.ui.lazylist.models

import com.arttttt.core.arch.base.ListItem

data class ProfileListItem(
    val id: String,
    val title: String,
    val color: Int,
    val isSelected: Boolean,
    override val key: Any = id,
) : ListItem