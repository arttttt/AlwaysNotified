package com.arttttt.alwaysnotified.ui.appslist.lazylist.delegates

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.arttttt.alwaysnotified.ui.appslist.lazylist.models.ProgressListItem
import com.arttttt.lazylist.dsl.lazyListDelegate
import com.arttttt.uikit.theme.AppTheme

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