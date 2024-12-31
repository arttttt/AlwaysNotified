package com.arttttt.appslist.impl.ui.lazylist.ui

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arttttt.appslist.impl.ui.lazylist.models.UnsupportedAppListItem
import com.arttttt.appslist.impl.ui.lazylist.models.fromClippableItem
import com.arttttt.uikit.theme.AppTheme
import com.google.accompanist.drawablepainter.rememberDrawablePainter

@Composable
internal fun UnsupportedAppItemContent(
    modifier: Modifier,
    item: UnsupportedAppListItem,
) {
    Column(
        modifier = modifier
            .fromClippableItem(item)
            .background(AppTheme.colors.tertiary)
            .padding(
                horizontal = 16.dp,
                vertical = 8.dp,
            )
            .animateContentSize()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
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

            Box(
                modifier = Modifier.fillMaxHeight()
            ) {
                Text(
                    text = "Not supported"
                )
            }
        }
    }
}