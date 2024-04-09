package com.arttttt.core.arch.base.delegate

import com.arttttt.core.arch.base.ListItem

abstract class LazyListDelegate<T : ListItem> {

    abstract fun getContentType(item: T): Any
    abstract fun getKey(item: T): Any

    abstract fun isForItem(item: ListItem): Boolean

    abstract fun createViewHolder(): LazyListViewHolder<T>
}