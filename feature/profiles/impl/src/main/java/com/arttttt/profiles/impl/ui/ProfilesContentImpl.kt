package com.arttttt.profiles.impl.ui

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
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.view.HapticFeedbackConstantsCompat
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.arttttt.core.arch.base.dsl.items
import com.arttttt.core.arch.base.dsl.rememberLazyListDelegateManager
import com.arttttt.core.arch.content.ComponentContent
import com.arttttt.core.arch.content.ComponentContentOwner
import com.arttttt.profiles.api.ProfilesComponent
import com.arttttt.profiles.impl.ui.lazylist.delegates.ProfileListDelegate
import com.arttttt.uikit.LocalCorrectHapticFeedback
import com.arttttt.uikit.theme.AppTheme
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

internal class ProfilesContentImpl(
    private val component: ProfilesComponent,
) : ComponentContent {

    @Composable
    override fun Content(modifier: Modifier) {
        val state by component.uiState.collectAsState()
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
                /*is ProfileActionsComponent -> ProfileActionsContent(
                    component = dialogComponent,
                )
                is RemoveProfileComponent -> RemoveProfileContent(
                    component = dialogComponent,
                )*/
                is ComponentContentOwner -> dialogComponent.content.Content(
                    modifier = Modifier,
                )
            }
        }

        val context = LocalContext.current
        LaunchedEffect(component) {
            component
                .commands
                .onEach { command ->
                    when (command) {
                        is ProfilesComponent.Command.ShowMessage -> {
                            Toast.makeText(context, command.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                .launchIn(this)
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
}