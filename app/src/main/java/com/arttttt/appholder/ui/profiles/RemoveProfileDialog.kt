package com.arttttt.appholder.ui.profiles

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.arttttt.appholder.ui.theme.AppTheme

@Composable
fun RemoveProfileDialog(
    onDismiss: () -> Unit,
    onPositiveClicked: () -> Unit
) {

    Dialog(
        onDismissRequest = onDismiss,
    ) {
        Column(
            modifier = Modifier
                .clip(AppTheme.shapes.roundedCorners.medium())
                .background(AppTheme.colors.secondary)
                .padding(
                    start = 16.dp,
                    top = 16.dp,
                    end = 16.dp,
                    bottom = 8.dp,
                )
        ) {
            Text(text = "Do you really want to remove the profile?")


            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
            ) {
                TextButton(
                    shape = AppTheme.shapes.roundedCorners.tiny(),
                    onClick = onPositiveClicked,
                ) {
                    Text(
                        text = "Remove",
                        color = AppTheme.colors.textAndIcons,
                    )
                }

                TextButton(
                    shape = AppTheme.shapes.roundedCorners.tiny(),
                    onClick = onDismiss,
                ) {
                    Text(
                        text = "Cancel",
                        color = AppTheme.colors.textAndIcons,
                    )
                }
            }
        }
    }
}