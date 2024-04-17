package com.arttttt.lazylist.delegate

import com.arttttt.lazylist.ListItem

abstract class LazyListDelegate<T : ListItem> {

    abstract fun getContentType(item: T): Any
    abstract fun getKey(item: T): Any

    abstract fun isForItem(item: ListItem): Boolean

    abstract fun createViewHolder(): LazyListViewHolder<T>
}