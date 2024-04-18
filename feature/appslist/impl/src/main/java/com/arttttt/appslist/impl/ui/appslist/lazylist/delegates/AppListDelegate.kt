package com.arttttt.appslist.impl.ui.appslist.lazylist.delegates

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arttttt.appslist.impl.ui.appslist.lazylist.models.AppListItem
import com.arttttt.appslist.impl.ui.appslist.lazylist.models.fromClippableItem
import com.arttttt.lazylist.dsl.lazyListDelegate
import com.arttttt.uikit.theme.AppTheme
import com.google.accompanist.drawablepainter.rememberDrawablePainter

internal fun AppListDelegate(
    onClick: (String) -> Unit,
    onManualModeChanged: (String) -> Unit,
) = lazyListDelegate<AppListItem> {

    Row(
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
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (item.icon != null) {
            Image(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape),
                painter = rememberDrawablePainter(item.icon),
                contentDescription = null,
            )

            Spacer(modifier = Modifier.width(16.dp))
        }

        Text(
            modifier = Modifier.weight(1f),
            text = item.title,
            fontSize = 18.sp,
        )

        Spacer(modifier = Modifier.width(16.dp))

        if (item.isManualModeAvailable) {
            Text(
                modifier = Modifier.weight(1f),
                text = "Manual mode",
                fontSize = 18.sp,
            )

            Switch(
                checked = item.manualMode,
                onCheckedChange = {
                    onManualModeChanged.invoke(item.pkg)
                },
                colors = AppTheme.widgets.switchColors,
            )
        }
    }
}