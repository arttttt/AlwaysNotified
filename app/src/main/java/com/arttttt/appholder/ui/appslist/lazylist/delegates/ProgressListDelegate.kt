package com.arttttt.appholder.ui.appslist.lazylist.delegates

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.arttttt.appholder.ui.appslist.lazylist.models.ProgressListItem
import com.arttttt.appholder.ui.base.dsl.lazyListDelegate

fun ProgressListDelegate() = lazyListDelegate<ProgressListItem> { scope, holder, modifier ->
    with(scope) {
        with(holder) {
           Box(
               modifier = Modifier.fillParentMaxSize(),
               contentAlignment = Alignment.Center,
           ) {
               CircularProgressIndicator()
           }
        }
    }
}