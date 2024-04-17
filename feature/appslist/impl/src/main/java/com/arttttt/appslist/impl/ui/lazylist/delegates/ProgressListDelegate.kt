package com.arttttt.appslist.impl.ui.lazylist.delegates

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.arttttt.appslist.impl.ui.lazylist.models.ProgressListItem
import com.arttttt.lazylist.dsl.lazyListDelegate
import com.arttttt.uikit.theme.AppTheme

internal fun ProgressListDelegate() = lazyListDelegate<ProgressListItem> {
    Box(
        modifier = Modifier.fillParentMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator(
            color = AppTheme.colors.secondary
        )
    }
}