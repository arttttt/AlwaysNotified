package com.arttttt.alwaysnotified.ui.appslist.lazylist.models

import android.graphics.drawable.Drawable
import com.arttttt.alwaysnotified.ui.base.ListItem

data class AppListItem(
    val pkg: String,
    val title: String,
    val icon: Drawable?,
    val manualMode: Boolean,
    val isManualModeAvailable: Boolean,
    override val clipTop: Boolean,
    override val clipBottom: Boolean,
    override val key: Any = pkg
) : ListItem, ClippableItem