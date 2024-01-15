package com.arttttt.appholder.ui.profiles

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.view.HapticFeedbackConstantsCompat
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.arttttt.appholder.components.profiles.ProfilesComponent
import com.arttttt.appholder.ui.base.dsl.items
import com.arttttt.appholder.ui.base.dsl.rememberLazyListDelegateManager
import com.arttttt.appholder.ui.custom.LocalCorrectHapticFeedback
import com.arttttt.appholder.ui.profiles.lazylist.delegates.ProfileListDelegate
import com.arttttt.appholder.ui.theme.AppTheme
import com.arttttt.appholder.utils.extensions.unsafeCastTo
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentSetOf

private sealed class Dialog {

    data object AddProfile : Dialog()

    data class RemoveProfile(val id: String) : Dialog()

    data class ProfileActions(val id: String) : Dialog()
}

/**
 * todo: show dialogs through the Slot API
 */
@Composable
fun ProfilesContent(component: ProfilesComponent) {
    val state by component.uiState.subscribeAsState()

    val haptics = LocalCorrectHapticFeedback.current

    var dialog: Dialog? by remember {
        mutableStateOf(null)
    }

    val lazyListDelegateManager = rememberLazyListDelegateManager(
        delegates = persistentListOf(
            ProfileListDelegate(
                onClick = component::profileClicked,
                longClick = { id ->
                    haptics.performHapticFeedback(HapticFeedbackConstantsCompat.VIRTUAL_KEY)

                    dialog = Dialog.ProfileActions(id)
                },
            ),
        ),
    )

    CompositionLocalProvider(
        LocalContentColor provides AppTheme.colors.primary
    ) {
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            item {
                AddButton(
                    onClick = {
                        dialog = Dialog.AddProfile
                    },
                )
            }

            items(
                lazyListDelegateManager = lazyListDelegateManager,
                items = state.items,
            )
        }
    }

    when (dialog) {
        is Dialog.AddProfile -> {
            AddProfileDialog(
                onDismiss = {
                    dialog = null
                },
                onPositiveClicked = { name, color ->
                    dialog = null
                    component.addProfile(name, color)
                },
            )
        }
        is Dialog.RemoveProfile -> {
            RemoveProfileDialog(
                onDismiss = {
                    dialog = null
                },
                onPositiveClicked = {
                    component.removeProfile(dialog!!.unsafeCastTo<Dialog.RemoveProfile>().id)
                    dialog = null
                }
            )
        }
        is Dialog.ProfileActions -> {
            val context = LocalContext.current

            ProfileActionsBottomSheet(
                onDismiss = {
                    dialog = null
                },
                actions = persistentSetOf(
                    ProfileAction(
                        title = "Rename",
                        icon = Icons.Default.Edit,
                        onClick = {
                            Toast.makeText(context, "under construction", Toast.LENGTH_SHORT).show()
                        },
                    ),
                    ProfileAction(
                        title = "Remove",
                        icon = Icons.Default.Delete,
                        onClick = {
                            dialog = Dialog.RemoveProfile(dialog!!.unsafeCastTo<Dialog.ProfileActions>().id)
                        },
                    ),
                )
            )
        }
        else -> {}
    }
}

@Composable
private fun AddButton(
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .size(48.dp)
            .clip(CircleShape)
            .background(AppTheme.colors.textAndIcons)
            .clickable(
                onClick = onClick,
            ),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = null,
        )
    }
}