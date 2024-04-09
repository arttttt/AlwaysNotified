package com.arttttt.alwaysnotified.ui.removeprofile

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.arttttt.alwaysnotified.components.removeprofile.RemoveProfileComponent
import com.arttttt.uikit.theme.AppTheme
import com.arttttt.core.arch.dialog.DismissEvent
import kotlinx.coroutines.flow.Flow

@Preview
@Composable
private fun RemoveProfileContentPreview() {
    RemoveProfileContent(
        component = object : RemoveProfileComponent {
            override fun onDismiss(event: DismissEvent) {
                TODO("Not yet implemented")
            }
            override val dismissEvents: Flow<DismissEvent>
                get() = TODO("Not yet implemented")

            override val events: Flow<RemoveProfileComponent.Event>
                get() = TODO("Not yet implemented")

            override fun confirmedClicked() {
                TODO("Not yet implemented")
            }
        }
    )
}

@Composable
fun RemoveProfileContent(
    component: RemoveProfileComponent,
) {

    Dialog(
        onDismissRequest = {
            component.onDismiss(DismissEvent)
        },
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
            Text(
                text = "Do you really want to remove the profile?",
                color = AppTheme.colors.textAndIcons,
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
            ) {
                TextButton(
                    shape = AppTheme.shapes.roundedCorners.tiny(),
                    onClick = component::confirmedClicked,
                ) {
                    Text(
                        text = "Remove",
                        color = AppTheme.colors.textAndIcons,
                    )
                }

                TextButton(
                    shape = AppTheme.shapes.roundedCorners.tiny(),
                    onClick = {
                        component.onDismiss(DismissEvent)
                    },
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