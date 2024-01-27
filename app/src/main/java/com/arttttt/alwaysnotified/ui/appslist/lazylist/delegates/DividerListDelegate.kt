package com.arttttt.alwaysnotified.ui.appslist.lazylist.delegates

import androidx.compose.ui.Modifier
import com.arttttt.alwaysnotified.ui.appslist.lazylist.models.DividerListItem
import com.arttttt.alwaysnotified.ui.base.dsl.lazyListDelegate
import com.arttttt.alwaysnotified.ui.common.Divider

fun DividerListDelegate() = lazyListDelegate<DividerListItem> {
    Divider(
        modifier = Modifier.fillParentMaxWidth()
    )
}