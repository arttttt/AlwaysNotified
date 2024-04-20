package com.arttttt.appslist.impl.ui.appslist.lazylist.models

import com.arttttt.lazylist.ListItem

internal data object ProgressListItem : ListItem {

    override val key: Any = hashCode()
}