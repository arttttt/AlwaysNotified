package com.arttttt.core.arch.base.delegate

import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.arttttt.core.arch.base.ListItem
import kotlinx.collections.immutable.ImmutableList

class LazyListDelegatesManager(
    private val delegates: ImmutableList<LazyListDelegate<ListItem>>
) {

    companion object {

        private const val NO_DELEGATE_EXCEPTION = "No delegate found for item: %s"
    }

    fun getKey(item: ListItem): Any {
        return getDelegateOrThrow(item).getKey(item)
    }

    fun getContentType(item: ListItem): Any {
        return getDelegateOrThrow(item).getContentType(item)
    }

    context(LazyItemScope)
    @Composable
    fun Content(item: ListItem, modifier: Modifier) {
        val delegate = remember(item.key) {
            getDelegateOrThrow(item)
        }

        val holder = remember(item.key) {
            delegate.createViewHolder()
        }

        holder.setItem(item)

        holder.Content(
            modifier = modifier,
        )
    }

    private fun getDelegateOrThrow(item: ListItem): LazyListDelegate<ListItem> {
        val delegate = delegates.find { delegate -> delegate.isForItem(item) }
        checkNotNull(delegate) {
            NO_DELEGATE_EXCEPTION.format(item::class.qualifiedName)
        }

        return delegate
    }
}