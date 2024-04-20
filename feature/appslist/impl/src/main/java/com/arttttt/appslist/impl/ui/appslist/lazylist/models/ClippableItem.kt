package com.arttttt.appslist.impl.ui.appslist.lazylist.models

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

internal interface ClippableItem {
    val clipTop: Boolean
    val clipBottom: Boolean
}

internal fun Modifier.fromClippableItem(item: ClippableItem): Modifier {
    return when {
        item.clipTop && item.clipBottom -> clip(
            RoundedCornerShape(
                topStart = 8.dp,
                topEnd = 8.dp,
                bottomStart = 8.dp,
                bottomEnd = 8.dp,
            )
        )
        item.clipTop -> clip(
            RoundedCornerShape(
                topStart = 8.dp,
                topEnd = 8.dp,
            )
        )
        item.clipBottom -> clip(
            RoundedCornerShape(
                bottomStart = 8.dp,
                bottomEnd = 8.dp,
            )
        )
        else -> this
    }
}