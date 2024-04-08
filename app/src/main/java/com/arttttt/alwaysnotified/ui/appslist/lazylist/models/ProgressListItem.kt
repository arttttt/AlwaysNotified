package com.arttttt.alwaysnotified.ui.appslist.lazylist.models

import com.arttttt.alwaysnotified.ui.base.ListItem

data object ProgressListItem : ListItem {

    override val key: Any = hashCode()
}