package com.arttttt.appslist.impl.ui.appslist

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.arttttt.appslist.impl.components.appslist.InternalAppsListComponent
import com.arttttt.appslist.impl.ui.appslist.lazylist.delegates.AppListDelegate
import com.arttttt.appslist.impl.ui.appslist.lazylist.delegates.DividerListDelegate
import com.arttttt.appslist.impl.ui.appslist.lazylist.delegates.ProgressListDelegate
import com.arttttt.core.arch.content.ComponentContent
import com.arttttt.core.arch.content.ComponentContentOwner
import com.arttttt.lazylist.ListItem
import com.arttttt.lazylist.dsl.rememberLazyListDelegateManager
import com.arttttt.uikit.theme.AppTheme
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

/**
 * todo: reorganize layout
 */
internal class AppsListContent(
    private val component: InternalAppsListComponent,
) : ComponentContent {

    @Composable
    override fun Content(modifier: Modifier) {
        val state by component.uiState.collectAsState()
        val slot by component.slot.subscribeAsState()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(AppTheme.colors.primary)
                .navigationBarsPadding()
        ) {
            component.topBarComponent.content.Content(modifier = Modifier)

            AppsListContainer(
                apps = state.apps,
                isStartButtonVisible = state.isStartButtonVisible,
                isUpdateProfileButtonVisible = state.isSaveProfileButtonVisible,
                onAppClicked = component::onAppClicked,
                onStartAppsClicked = component::startApps,
                onUpdateProfileClicked = component::updateProfile,
                onManualModeChanged = component::onManualModeChanged,
            )
        }

        slot.child?.instance?.let { instance ->
            when (instance) {
                is ComponentContentOwner -> instance.content.Content(modifier = Modifier)
            }
        }
    }

    @Composable
    private fun AppsListContainer(
        apps: ImmutableList<ListItem>,
        isUpdateProfileButtonVisible: Boolean,
        isStartButtonVisible: Boolean,
        onAppClicked: (String) -> Unit,
        onStartAppsClicked: () -> Unit,
        onUpdateProfileClicked: () -> Unit,
        onManualModeChanged: (String) -> Unit,
    ) {
        var parentCoordinates: LayoutCoordinates? by remember {
            mutableStateOf(null)
        }

        val currentIsUpdateProfileButtonVisible by rememberUpdatedState(isUpdateProfileButtonVisible)
        val currentIsStartButtonVisible by rememberUpdatedState(isStartButtonVisible)

        Box(
            modifier = Modifier
                .fillMaxSize()
                .onGloballyPositioned { coordinates ->
                    parentCoordinates = coordinates
                }
        ) {
            val transitionState = remember {
                MutableTransitionState(isStartButtonVisible || isUpdateProfileButtonVisible)
            }

            val nestedScrollConnection = remember {
                object : NestedScrollConnection {
                    override fun onPreScroll(
                        available: Offset,
                        source: NestedScrollSource,
                    ): Offset {
                        if ((currentIsUpdateProfileButtonVisible || currentIsStartButtonVisible) && available.y != 0f) {
                            transitionState.targetState = available.y > 0
                        }

                        return Offset.Zero
                    }
                }
            }

            AppsList(
                modifier = Modifier
                    .nestedScroll(nestedScrollConnection)
                    .matchParentSize()
                    .padding(
                        horizontal = 16.dp
                    ),
                apps = apps,
                onAppClicked = onAppClicked,
                onManualModeChanged = onManualModeChanged,
            )

            AnimatedVisibility(
                modifier = Modifier.align(Alignment.BottomStart),
                enter = slideInVertically { it } + fadeIn(),
                exit = slideOutVertically { it } + fadeOut(),
                visibleState = transitionState,
            ) {
                ActionsRow(
                    modifier = Modifier,
                    isSaveProfileButtonVisible = isUpdateProfileButtonVisible,
                    isStartAppsButtonVisible = isStartButtonVisible,
                    onStartAppsClicked = onStartAppsClicked,
                    onUpdateProfileClicked = onUpdateProfileClicked,
                )
            }

            LaunchedEffect(isStartButtonVisible, isUpdateProfileButtonVisible) {
                transitionState.targetState = isStartButtonVisible || isUpdateProfileButtonVisible
            }
        }
    }

    @Composable
    private fun AppsList(
        modifier: Modifier,
        apps: ImmutableList<ListItem>,
        onAppClicked: (String) -> Unit,
        onManualModeChanged: (String) -> Unit
    ) {
        val lazyListDelegateManager = rememberLazyListDelegateManager(
            delegates = persistentListOf(
                AppListDelegate(
                    onClick = onAppClicked,
                    onManualModeChanged = onManualModeChanged,
                ),
                DividerListDelegate(),
                ProgressListDelegate(),
            ),
        )

        LazyColumn(
            modifier = modifier,
            contentPadding = remember {
                PaddingValues(vertical = 8.dp)
            }
        ) {
            items(
                items = apps,
                key = lazyListDelegateManager::getKey,
                contentType = lazyListDelegateManager::getContentType
            ) { item ->
                lazyListDelegateManager.Content(
                    item = item,
                    modifier = Modifier,
                )
            }
        }
    }

    @Composable
    private fun ActionsRow(
        modifier: Modifier,
        isSaveProfileButtonVisible: Boolean,
        isStartAppsButtonVisible: Boolean,
        onStartAppsClicked: () -> Unit,
        onUpdateProfileClicked: () -> Unit,
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .clip(
                    AppTheme.shapes.roundedCorners.medium(
                        bottomStart = 0.dp,
                        bottomEnd = 0.dp,
                    )
                )
                .background(AppTheme.colors.secondary)
                .padding(
                    horizontal = 16.dp,
                    vertical = 16.dp,
                ),
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
        ) {
            if (isSaveProfileButtonVisible) {
                Button(
                    modifier = Modifier.weight(1f),
                    onClick = onUpdateProfileClicked,
                    colors = AppTheme.widgets.buttonColors,
                ) {
                    Text(text = stringResource(com.arttttt.localization.R.string.update_profile))
                }
            }

            if (isStartAppsButtonVisible) {
                Button(
                    modifier = Modifier.weight(1f),
                    onClick = onStartAppsClicked,
                    colors = AppTheme.widgets.buttonColors,
                ) {
                    Text(text = stringResource(com.arttttt.localization.R.string.start_apps))
                }
            }
        }
    }
}