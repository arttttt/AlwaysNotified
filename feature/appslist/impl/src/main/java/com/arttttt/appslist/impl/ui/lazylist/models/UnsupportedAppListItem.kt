package com.arttttt.appslist.impl.ui.lazylist.models

import android.graphics.drawable.Drawable
import com.arttttt.lazylist.ListItem

internal data class UnsupportedAppListItem(
    val pkg: String,
    val title: String,
    val icon: Drawable?,
    override val clipTop: Boolean,
    override val clipBottom: Boolean,
) : ListItem, ClippableItem {

    override val key: Any by this::pkg
}