package com.arttttt.appslist.impl.ui.app.lazylist.delegates

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp

@Composable
internal fun SectionTitleContent(
    modifier: Modifier,
    text: String,
) {

    Text(
        modifier = modifier.padding(
            horizontal = 16.dp,
            vertical = 8.dp,
        ),
        text = text,
        style = MaterialTheme.typography.titleLarge,
    )
}

@PreviewLightDark
@Composable
private fun SectionTitleContentPreview() {
    SectionTitleContent(
        modifier = Modifier.fillMaxWidth(),
        text = "Title",
    )
}