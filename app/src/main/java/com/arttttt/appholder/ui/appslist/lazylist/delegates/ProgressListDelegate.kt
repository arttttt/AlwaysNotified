package com.arttttt.appholder.ui.appslist.lazylist.delegates

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.arttttt.appholder.ui.appslist.lazylist.models.ProgressListItem
import com.arttttt.appholder.ui.base.dsl.lazyListDelegate
import com.arttttt.appholder.ui.theme.AppTheme

fun ProgressListDelegate() = lazyListDelegate<ProgressListItem> {
    Box(
        modifier = Modifier.fillParentMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator(
            color = AppTheme.colors.secondary
        )
    }
}