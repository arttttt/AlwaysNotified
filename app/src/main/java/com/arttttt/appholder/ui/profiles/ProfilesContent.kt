package com.arttttt.appholder.ui.profiles

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
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.core.view.HapticFeedbackConstantsCompat
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.arttttt.appholder.components.addprofile.AddProfileComponent
import com.arttttt.appholder.components.profileactions.ProfileActionsComponent
import com.arttttt.appholder.components.profiles.ProfilesComponent
import com.arttttt.appholder.components.removeprofile.RemoveProfileComponent
import com.arttttt.appholder.ui.addprofile.AddProfileContent
import com.arttttt.appholder.ui.base.dsl.items
import com.arttttt.appholder.ui.base.dsl.rememberLazyListDelegateManager
import com.arttttt.appholder.ui.custom.LocalCorrectHapticFeedback
import com.arttttt.appholder.ui.profileactions.ProfileActionsContent
import com.arttttt.appholder.ui.profiles.lazylist.delegates.ProfileListDelegate
import com.arttttt.appholder.ui.removeprofile.RemoveProfileContent
import com.arttttt.appholder.ui.theme.AppTheme
import kotlinx.collections.immutable.persistentListOf

@Composable
fun ProfilesContent(component: ProfilesComponent) {
    val state by component.uiState.subscribeAsState()
    val dialog by component.dialog.subscribeAsState()

    val haptics = LocalCorrectHapticFeedback.current

    val lazyListDelegateManager = rememberLazyListDelegateManager(
        delegates = persistentListOf(
            ProfileListDelegate(
                onClick = component::profileClicked,
                longClick = { id ->
                    haptics.performHapticFeedback(HapticFeedbackConstantsCompat.VIRTUAL_KEY)

                    component.profileActionsLongPressed(id)
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
                    onClick = component::addProfileClicked,
                )
            }

            items(
                lazyListDelegateManager = lazyListDelegateManager,
                items = state.items,
            )
        }
    }

    dialog.child?.instance?.let { dialogComponent ->
        when (dialogComponent) {
            is ProfileActionsComponent -> ProfileActionsContent(
                component = dialogComponent,
            )
            is RemoveProfileComponent -> RemoveProfileContent(
                component = dialogComponent,
            )
            is AddProfileComponent -> AddProfileContent(
                component = dialogComponent,
            )
        }
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