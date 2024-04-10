package com.arttttt.alwaysnotified.ui.appslist.lazylist.models

import com.arttttt.lazylist.ListItem

data object ProgressListItem : ListItem {

    override val key: Any = hashCode()
}