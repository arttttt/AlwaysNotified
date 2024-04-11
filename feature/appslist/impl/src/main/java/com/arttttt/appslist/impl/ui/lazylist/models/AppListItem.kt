package com.arttttt.appslist.impl.ui.lazylist.models

import android.graphics.drawable.Drawable
import com.arttttt.lazylist.ListItem

internal data class AppListItem(
    val pkg: String,
    val title: String,
    val icon: Drawable?,
    val manualMode: Boolean,
    val isManualModeAvailable: Boolean,
    override val clipTop: Boolean,
    override val clipBottom: Boolean,
    override val key: Any = pkg
) : ListItem, ClippableItem