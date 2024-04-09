package com.arttttt.core.arch.base.dsl

import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.arttttt.core.arch.base.ListItem
import com.arttttt.core.arch.base.delegate.LazyListDelegate
import com.arttttt.core.arch.base.delegate.LazyListDelegatesManager
import com.arttttt.core.arch.base.delegate.LazyListViewHolder
import kotlinx.collections.immutable.ImmutableList

inline fun <reified T : ListItem> lazyListDelegate(
    noinline key: (item: T) -> Any = { item -> item.key },
    noinline contentType: ((item: T) -> Any) = { it::class },
    noinline content: @Composable context(LazyItemScope, LazyListViewHolder<T>) (Modifier) -> Unit,
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

    override fun createViewHolder(): LazyListViewHolder<T> {
        return object : LazyListViewHolder<T>() {

            context(LazyItemScope)
            @Composable
            override fun Content(modifier: Modifier) {
                content(this@LazyItemScope, this, modifier)
            }
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

@Suppress("NOTHING_TO_INLINE")
inline fun LazyListScope.items(
    lazyListDelegateManager: LazyListDelegatesManager,
    items: List<ListItem>,
    noinline key: (item: ListItem) -> Any = lazyListDelegateManager::getKey,
    noinline contentType: (item: ListItem) -> Any? = lazyListDelegateManager::getContentType,
) {
    items(
        items = items,
        key = key,
        contentType = contentType,
    ) { item ->
        lazyListDelegateManager.Content(
            item = item,
            modifier = Modifier,
        )
    }
}