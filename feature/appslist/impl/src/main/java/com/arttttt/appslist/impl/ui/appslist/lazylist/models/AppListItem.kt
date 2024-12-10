package com.arttttt.appslist.impl.ui.appslist.lazylist.models

import android.graphics.drawable.Drawable
import com.arttttt.lazylist.ListItem

internal data class AppListItem(
    val pkg: String,
    val title: String,
    val icon: Drawable?,
    val isSelected: Boolean,
    override val clipTop: Boolean,
    override val clipBottom: Boolean,
) : ListItem, ClippableItem {

    override val key: Any by this::pkg
}