package com.arttttt.alwaysnotified.ui.appslist.lazylist.models

import com.arttttt.core.arch.base.ListItem

data object ProgressListItem : ListItem {

    override val key: Any = hashCode()
}