package com.arttttt.alwaysnotified.ui.permissions.content

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview(
    showBackground = true,
)
@Composable
private fun GrantedPermissionContentPreview() {
    GrantedPermissionContent(
        modifier = Modifier.fillMaxWidth(),
        title = "Test permission",
    )
}

@Composable
fun GrantedPermissionContent(
    modifier: Modifier,
    title: String
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

        Text(
            text = "Granted",
        )

        Spacer(modifier = Modifier.width(8.dp))

        Icon(
            imageVector = Icons.Filled.CheckCircle,
            contentDescription = null,
            tint = Color(0xff5bb450),
        )
    }
}