package com.arttttt.permissions.impl.ui.content

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.arttttt.uikit.theme.AppTheme

@Preview(
    showBackground = true,
)
@Composable
private fun DeniedPermissionContentPreview() {
    DeniedPermissionContent(
        modifier = Modifier.fillMaxWidth(),
        title = "Test permission",
        onClick = {},
    )
}

@Composable
internal fun DeniedPermissionContent(
    modifier: Modifier,
    title: String,
    onClick: () -> Unit,
) {
    Row(
        modifier = modifier.padding(
            horizontal = 16.dp,
            vertical = 8.dp
        ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = title
        )

        Spacer(modifier = Modifier.weight(1f))

        Button(
            colors = AppTheme.widgets.buttonColors,
            onClick = {
                onClick.invoke()
            }
        ) {
            Text(
                text = "Grant"
            )
        }
    }
}