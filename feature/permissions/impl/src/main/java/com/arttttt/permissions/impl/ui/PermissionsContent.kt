package com.arttttt.permissions.impl.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.arttttt.core.arch.content.ComponentContent
import com.arttttt.permissions.impl.components.InternalPermissionsComponent
import com.arttttt.permissions.impl.ui.content.DeniedPermissionContent
import com.arttttt.permissions.impl.ui.content.GrantedPermissionContent
import com.arttttt.permissions.impl.ui.models.PermissionLazyListItem
import com.arttttt.uikit.theme.AppTheme
import com.arttttt.uikit.widgets.Divider
import com.arttttt.uikit.widgets.EqualHeightColumn

internal class PermissionsContent(
    private val component: InternalPermissionsComponent,
) : ComponentContent {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content(modifier: Modifier) {
        val state by component.state.subscribeAsState()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(AppTheme.colors.primary)
        ) {
            TopAppBar(
                modifier = Modifier.clip(
                    AppTheme.shapes.roundedCorners.medium(
                        topStart = 0.dp,
                        topEnd = 0.dp,
                    )
                ),
                colors = AppTheme.widgets.topAppBarColors,
                title = {
                    Text(text = stringResource(com.arttttt.localization.R.string.permissions))
                }
            )

            val shapes = AppTheme.shapes
            val topCorners = remember {
                shapes.roundedCorners.medium(
                    bottomStart = 0.dp,
                    bottomEnd = 0.dp,
                )
            }

            val bottomCorners = remember {
                shapes.roundedCorners.medium(
                    topStart = 0.dp,
                    topEnd = 0.dp,
                )
            }

            val permissionModifier = Modifier
                .fillMaxWidth()
                .background(AppTheme.colors.tertiary)

            EqualHeightColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                spacing = 0.dp,
            ) {
                state.items.forEachIndexed { index, item ->
                    when (item) {
                        is PermissionLazyListItem.Granted -> {
                            key(item.key) {
                                GrantedPermissionContent(
                                    modifier = Modifier
                                        .clip(
                                            index = index,
                                            itemsCount = state.items.size,
                                            topCorners = topCorners,
                                            bottomCorners = bottomCorners,
                                        )
                                        .then(permissionModifier),
                                    title = item.title,
                                )
                            }
                        }
                        is PermissionLazyListItem.Denied -> {
                            key(item.key) {
                                DeniedPermissionContent(
                                    modifier = Modifier
                                        .clip(
                                            index = index,
                                            itemsCount = state.items.size,
                                            topCorners = topCorners,
                                            bottomCorners = bottomCorners,
                                        )
                                        .then(permissionModifier),
                                    title = item.title,
                                    onClick = remember {
                                        {
                                            component.grantPermissionClicked(item.permission)
                                        }
                                    },
                                )
                            }
                        }
                    }

                    if (index != state.items.size - 1) {
                        Divider(
                            modifier = Modifier
                                .fillMaxWidth()
                                .ignoreEqualHeight()
                        )
                    }
                }
            }
        }
    }

    private fun Modifier.clip(
        index: Int,
        itemsCount: Int,
        topCorners: Shape,
        bottomCorners: Shape,
    ): Modifier {
        return when {
            index == 0 && itemsCount == 1 -> this.clip(topCorners).clip(bottomCorners)
            index == 0 -> this.clip(topCorners)
            index == itemsCount - 1 -> this.clip(bottomCorners)
            else -> this
        }
    }
}