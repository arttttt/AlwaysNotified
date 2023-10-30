package com.arttttt.appholder.ui.appslist.lazylist.delegates

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arttttt.appholder.ui.appslist.lazylist.models.AppListItem
import com.arttttt.appholder.ui.appslist.lazylist.models.fromClippableItem
import com.arttttt.appholder.ui.base.dsl.lazyListDelegate
import com.arttttt.appholder.ui.theme.AppTheme

fun AppListDelegate(
    onClick: (String) -> Unit
) = lazyListDelegate<AppListItem> {

    Text(
        modifier = Modifier
            .fillParentMaxWidth()
            .fromClippableItem(item)
            .background(AppTheme.colors.tertiary)
            .clickable {
                onClick.invoke(item.pkg)
            }
            .padding(
                horizontal = 16.dp,
                vertical = 8.dp,
            ),
        text = item.title,
        fontSize = 18.sp,
    )
}