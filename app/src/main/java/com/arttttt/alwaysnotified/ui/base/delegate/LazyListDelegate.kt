package com.arttttt.alwaysnotified.ui.base.delegate

import com.arttttt.alwaysnotified.ui.base.ListItem

abstract class LazyListDelegate<T : ListItem> {

    abstract fun getContentType(item: T): Any
    abstract fun getKey(item: T): Any

    abstract fun isForItem(item: ListItem): Boolean

    abstract fun createViewHolder(): LazyListViewHolder<T>
}