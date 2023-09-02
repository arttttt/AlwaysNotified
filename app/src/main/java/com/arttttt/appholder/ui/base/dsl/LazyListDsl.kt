package com.arttttt.appholder.ui.base.dsl

import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.arttttt.appholder.ui.base.ListItem
import com.arttttt.appholder.ui.base.delegate.LazyListDelegate
import com.arttttt.appholder.ui.base.delegate.LazyListDelegatesManager
import com.arttttt.appholder.ui.base.delegate.LazyListViewHolder
import kotlinx.collections.immutable.ImmutableList

inline fun <reified T : ListItem> lazyListDelegate(
    noinline key: (item: T) -> Any = { item -> item.key },
    noinline contentType: ((item: T) -> Any) = { it::class },
    noinline content: @Composable (LazyItemScope, LazyListViewHolder<T>, Modifier) -> Unit,
) = object : LazyListDelegate<T>() {

    override fun getContentType(item: T): Any {
        return contentType.invoke(item)
    }

    override fun getKey(item: T): Any {
        return key.invoke(item)
    }

    override fun isForItem(item: ListItem): Boolean {
        return item is T
    }

    override fun createViewHolder(item: T): LazyListViewHolder<T> {
        return object : LazyListViewHolder<T>() {
            @Composable
            override fun Content(context: LazyItemScope, modifier: Modifier) {
                content.invoke(context, this, modifier)
            }
        }.apply {
            _item = item
        }
    }
}

@Suppress("UNCHECKED_CAST")
@Composable
fun rememberLazyListDelegateManager(delegates: ImmutableList<LazyListDelegate<*>>): LazyListDelegatesManager {
    return remember(delegates) {
        LazyListDelegatesManager(
            delegates = delegates as ImmutableList<LazyListDelegate<ListItem>>
        )
    }
}