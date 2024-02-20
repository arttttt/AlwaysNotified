package com.arttttt.alwaysnotified.ui.topbar

import android.widget.Toast
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.arttttt.alwaysnotified.R
import com.arttttt.alwaysnotified.components.topbar.TopBarComponent
import com.arttttt.alwaysnotified.ui.theme.AppTheme
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@Composable
fun TopBarContent(component: TopBarComponent) {
    val state by component.uiState.subscribeAsState()

    ExpandableTopBar(
        modifier = Modifier.clip(
            AppTheme.shapes.roundedCorners.medium(
                topStart = 0.dp,
                topEnd = 0.dp,
            )
        ),
        uiState = state,
        title = {
            Text(
                text = stringResource(R.string.apps_list),
                fontSize = 22.sp,
            )
        },
        actions = {
            state.actions.forEach { action ->
                IconButton(
                    onClick = {
                        component.actionClicked(action)
                    }
                ) {
                    Icon(
                        imageVector = action.icon,
                        contentDescription = null
                    )
                }
            }
        }
    )

    val context = LocalContext.current
    LaunchedEffect(component) {
        component
            .commands
            .onEach { command ->
                when (command) {
                    is TopBarComponent.Command.ShowMessage -> {
                        Toast.makeText(context, command.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
            .launchIn(this)
    }
}

@Composable
private fun ExpandableTopBar(
    modifier: Modifier,
    uiState: TopBarComponent.UiState,
    title: @Composable () -> Unit,
    actions: @Composable () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(AppTheme.colors.secondary)
            .animateContentSize()
    ) {
        TopBar(
            title = title,
            actions = actions,
        )

        if (uiState.expandedAction != null) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 16.dp,
                    )
                    .padding(
                        bottom = 8.dp,
                    ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                uiState.expandedAction.Content()
            }
        }
    }
}

@Composable
private fun TopBar(
    title: @Composable () -> Unit,
    actions: @Composable () -> Unit,
) {
    Row(
        modifier = Modifier
            .systemBarsPadding()
            .fillMaxWidth()
            .height(64.dp)
            .padding(
                horizontal = 4.dp
            )
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                /**
                 * if icon is present
                 */
                /*.padding(
                    horizontal = 4.dp
                )*/
                /**
                 * if icon is absent
                 */
                .padding(
                    start = 12.dp,
                )
                .weight(1f),
            contentAlignment = Alignment.CenterStart
        ) {
            CompositionLocalProvider(
                LocalTextStyle provides AppTheme.typography.topBar
            ) {
                title()
            }
        }

        Row(
            modifier = Modifier.fillMaxHeight(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            actions()
        }
    }
}