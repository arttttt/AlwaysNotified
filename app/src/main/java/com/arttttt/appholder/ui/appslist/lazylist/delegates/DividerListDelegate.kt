package com.arttttt.appholder.ui.appslist.lazylist.delegates

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.arttttt.appholder.ui.appslist.lazylist.models.DividerListItem
import com.arttttt.appholder.ui.base.dsl.lazyListDelegate
import com.arttttt.appholder.ui.common.Divider
import com.arttttt.appholder.ui.theme.AppTheme

fun DividerListDelegate() = lazyListDelegate<DividerListItem> { scope, holder, modifier ->
    with(scope) {
        Divider(
            modifier = Modifier.fillParentMaxWidth()
        )
    }
}