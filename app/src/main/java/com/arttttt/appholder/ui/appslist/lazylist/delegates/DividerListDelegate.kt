package com.arttttt.appholder.ui.appslist.lazylist.delegates

import androidx.compose.ui.Modifier
import com.arttttt.appholder.ui.appslist.lazylist.models.DividerListItem
import com.arttttt.appholder.ui.base.dsl.lazyListDelegate
import com.arttttt.appholder.ui.common.Divider

fun DividerListDelegate() = lazyListDelegate<DividerListItem> {
    Divider(
        modifier = Modifier.fillParentMaxWidth()
    )
}