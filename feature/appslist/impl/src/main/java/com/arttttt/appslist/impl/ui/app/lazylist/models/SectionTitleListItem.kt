package com.arttttt.appslist.impl.ui.app.lazylist.models

import androidx.compose.runtime.Immutable
import com.arttttt.lazylist.ListItem

@Immutable
internal data class SectionTitleListItem(
    val title: String,
) : ListItem {

    override val key: Any by this::title
}