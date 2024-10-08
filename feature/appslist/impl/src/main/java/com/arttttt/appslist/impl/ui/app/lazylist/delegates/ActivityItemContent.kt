package com.arttttt.appslist.impl.ui.app.lazylist.delegates

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arttttt.appslist.impl.ui.app.lazylist.models.ActivityListItem
import com.arttttt.lazylist.dsl.lazyListDelegate
import com.arttttt.uikit.theme.AppTheme

@Composable
internal fun ActivityItemContent(
    modifier: Modifier,
    item: ActivityListItem,
    onClick: (String) -> Unit,
) {
    Row(
        modifier = modifier
            .clickable {
                onClick.invoke(item.name)
            }
            .padding(
                horizontal = 16.dp,
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = item.title,
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.width(8.dp))

        Switch(
            checked = item.isSelected,
            colors = AppTheme.widgets.switchColors,
            onCheckedChange = {
                onClick.invoke(item.name)
            },
        )
    }
}