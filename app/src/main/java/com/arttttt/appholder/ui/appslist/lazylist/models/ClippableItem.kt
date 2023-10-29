package com.arttttt.appholder.ui.appslist.lazylist.models

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

interface ClippableItem {
    val clipTop: Boolean
    val clipBottom: Boolean
}

fun Modifier.fromClippableItem(item: ClippableItem): Modifier {
    return if (item.clipTop) {
        clip(
            RoundedCornerShape(
                topStart = 8.dp,
                topEnd = 8.dp,
            )
        )
    } else if (item.clipBottom) {
        clip(
            RoundedCornerShape(
                bottomStart = 8.dp,
                bottomEnd = 8.dp,
            )
        )
    } else {
        this
    }
}