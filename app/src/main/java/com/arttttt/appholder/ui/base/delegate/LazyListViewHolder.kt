package com.arttttt.appholder.ui.base.delegate

import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arttttt.appholder.ui.base.ListItem

abstract class LazyListViewHolder<T : ListItem> {

    private object Uninitialized

    @PublishedApi
    internal var _item: Any = Uninitialized

    open val contentType: Any
        get() {
            return item::class
        }

    /**
     * Get the current bound item.
     */
    val item: T
        get() = if (_item === Uninitialized) {
            throw IllegalArgumentException(
                "Item has not been set yet"
            )
        } else {
            @Suppress("UNCHECKED_CAST")
            _item as T
        }

    @Composable
    abstract fun Content(context: LazyItemScope, modifier: Modifier)
}