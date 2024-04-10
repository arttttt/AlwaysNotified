package com.arttttt.alwaysnotified.ui.appslist.lazylist.delegates

import androidx.compose.ui.Modifier
import com.arttttt.alwaysnotified.ui.appslist.lazylist.models.DividerListItem
import com.arttttt.core.arch.base.dsl.lazyListDelegate
import com.arttttt.uikit.widgets.Divider

fun DividerListDelegate() = lazyListDelegate<DividerListItem> {
    Divider(
        modifier = Modifier.fillParentMaxWidth()
    )
}