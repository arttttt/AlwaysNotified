package com.arttttt.addprofile.impl.ui

import android.widget.Toast
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.arttttt.addprofile.api.AddProfileComponent
import com.arttttt.core.arch.content.ComponentContent
import com.arttttt.core.arch.dialog.DismissEvent
import com.arttttt.uikit.theme.AppTheme
import kotlin.math.abs

internal class AddProfileContentImpl(
    private val component: AddProfileComponent,
) : ComponentContent {

    /**
     * TODO: move out logic from the UI
     */
    @Composable
    override fun Content(modifier: Modifier) {
        var title by remember {
            mutableStateOf("")
        }

        var addSelectedApps by remember {
            mutableStateOf(false)
        }

        val isInError by produceState(initialValue = false, title) {
            value = !component.canCreateProfile(title)
        }

        val context = LocalContext.current

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
                    ),
            ) {
                Text(text = stringResource(com.arttttt.localization.R.string.add_profile))

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    isError = isInError,
                    modifier = Modifier.fillMaxWidth(),
                    value = title,
                    onValueChange = { title = it },
                    colors = OutlinedTextFieldDefaults.colors(
                        cursorColor = AppTheme.colors.textAndIcons,
                        focusedBorderColor = AppTheme.colors.textAndIcons,
                        focusedTextColor = AppTheme.colors.textAndIcons,
                        unfocusedBorderColor = AppTheme.colors.textAndIcons,
                        unfocusedTextColor = AppTheme.colors.textAndIcons,
                        errorTextColor = AppTheme.colors.textAndIcons,
                        errorCursorColor = AppTheme.colors.textAndIcons,
                    ),
                    supportingText = {
                        if (isInError) {
                            Text(
                                text = stringResource(com.arttttt.localization.R.string.profile_exists_error)
                            )
                        }
                    },
                    trailingIcon = {
                        if (isInError) {
                            Icon(
                                painter = rememberVectorPainter(image = Icons.Default.Info),
                                contentDescription = null,
                            )
                        }
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = stringResource(com.arttttt.localization.R.string.color)
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

                Spacer(modifier = Modifier.height(8.dp))

                SwitchWithTextRow(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(com.arttttt.localization.R.string.add_selected_apps),
                    checked = addSelectedApps,
                    onCheckedChange = { isChecked -> addSelectedApps = isChecked }
                )

                Spacer(modifier = Modifier.height(16.dp))

                DialogButtonsRow(
                    positiveButtonText = stringResource(com.arttttt.localization.R.string.add),
                    negativeButtonText = stringResource(com.arttttt.localization.R.string.cancel),
                    onPositiveClicked = {
                        if (isInError) {
                            Toast.makeText(
                                context,
                                context.getString(com.arttttt.localization.R.string.can_not_save_profile),
                                Toast.LENGTH_SHORT,
                            ).show()
                        } else {
                            component.createProfileClicked(
                                title = title,
                                color = title.toComposeColor().toArgb(),
                                addSelectedApps = addSelectedApps,
                            )
                        }
                    },
                    onNegativeClicked = {
                        component.onDismiss(DismissEvent)
                    },
                )
            }
        }
    }

    @Composable
    private fun SwitchWithTextRow(
        modifier: Modifier,
        text: String,
        checked: Boolean,
        onCheckedChange: (Boolean) -> Unit,
    ) {
        Row(
            modifier = modifier,
            verticalAlignment = Alignment.CenterVertically,
        ){
            Text(text = text)

            Spacer(modifier = Modifier.weight(1f))

            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange,
                colors = AppTheme.widgets.switchColors,
            )
        }
    }

    @Composable
    private fun DialogButtonsRow(
        positiveButtonText: String,
        negativeButtonText: String,
        onPositiveClicked: () -> Unit,
        onNegativeClicked: () -> Unit,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
        ) {
            TextButton(
                shape = AppTheme.shapes.roundedCorners.tiny(),
                onClick = onPositiveClicked,
            ) {
                Text(
                    text = positiveButtonText,
                    color = AppTheme.colors.textAndIcons,
                )
            }

            TextButton(
                shape = AppTheme.shapes.roundedCorners.tiny(),
                onClick = onNegativeClicked,
            ) {
                Text(
                    text = negativeButtonText,
                    color = AppTheme.colors.textAndIcons,
                )
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
}