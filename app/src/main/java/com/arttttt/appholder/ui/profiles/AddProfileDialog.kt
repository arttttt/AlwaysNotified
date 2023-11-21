package com.arttttt.appholder.ui.profiles

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.arttttt.appholder.ui.theme.AppTheme
import kotlin.math.abs

@Preview
@Composable
private fun AddProfileDialogPreview() {
    AppTheme {
        AddProfileDialog(
            onDismiss = {},
            onPositiveClicked = { _, _ -> }
        )
    }
}

@Composable
fun AddProfileDialog(
    onDismiss: () -> Unit,
    onPositiveClicked: (String, Int) -> Unit
) {
    var title by remember {
        mutableStateOf("")
    }

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
                ),
        ) {
            Text(text = "Add profile")

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = title,
                onValueChange = { title = it },
                colors = OutlinedTextFieldDefaults.colors(
                    cursorColor = AppTheme.colors.textAndIcons,
                    focusedBorderColor = AppTheme.colors.textAndIcons,
                    focusedTextColor = AppTheme.colors.textAndIcons,
                    unfocusedBorderColor = AppTheme.colors.textAndIcons,
                    unfocusedTextColor = AppTheme.colors.textAndIcons,
                ),
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "Color"
                )

                Spacer(modifier = Modifier.width(16.dp))

                Box(
                    modifier = Modifier
                        .height(
                            height = 30.dp,
                        )
                        .fillMaxWidth()
                        .background(title.toComposeColor())
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
            ) {
                TextButton(
                    shape = AppTheme.shapes.roundedCorners.tiny(),
                    onClick = {
                        onPositiveClicked.invoke(title, title.toComposeColor().toArgb())
                    },
                ) {
                    Text(
                        text = "Add",
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

private fun String.toComposeColor(): Color {
    return when {
        isEmpty() -> Color.Black
        else -> {
            val hash = this.hashCode()
            val positiveHash = abs(hash)
            val rgb = positiveHash and 0xFFFFFF
            Color(0xFF000000 or rgb.toLong())
        }
    }
}