package com.arttttt.appholder.ui.base.delegate

import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arttttt.appholder.ui.base.ListItem

abstract class LazyListViewHolder<T : ListItem> {

    private object Uninitialized

    private var _item: Any = Uninitialized

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

    context(LazyItemScope)
    @Composable
    abstract fun Content(modifier: Modifier)

    fun setItem(item: T) {
        if (item == _item) return

        _item = item
    }
}