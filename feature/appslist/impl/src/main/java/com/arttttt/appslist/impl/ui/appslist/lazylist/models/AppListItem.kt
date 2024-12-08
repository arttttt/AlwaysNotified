package com.arttttt.appslist.impl.ui.appslist.lazylist.models

import android.graphics.drawable.Drawable
import com.arttttt.lazylist.ListItem

internal data class AppListItem(
    val pkg: String,
    val title: String,
    val icon: Drawable?,
    override val clipTop: Boolean,
    override val clipBottom: Boolean,
    override val key: Any = pkg
) : ListItem, ClippableItem