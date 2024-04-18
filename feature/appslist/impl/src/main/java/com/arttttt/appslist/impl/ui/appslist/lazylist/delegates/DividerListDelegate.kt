package com.arttttt.appslist.impl.ui.appslist.lazylist.delegates

import androidx.compose.ui.Modifier
import com.arttttt.appslist.impl.ui.appslist.lazylist.models.DividerListItem
import com.arttttt.lazylist.dsl.lazyListDelegate
import com.arttttt.uikit.widgets.Divider

internal fun DividerListDelegate() = lazyListDelegate<DividerListItem> {
    Divider(
        modifier = Modifier.fillParentMaxWidth()
    )
}