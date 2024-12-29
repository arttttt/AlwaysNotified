package com.arttttt.appslist.impl.ui.lazylist.models

import com.arttttt.lazylist.ListItem

internal data object ProgressListItem : ListItem {

    override val key: Any = hashCode()
}