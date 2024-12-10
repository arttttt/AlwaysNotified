package com.arttttt.appslist.impl.ui.appslist

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import com.arttttt.appslist.impl.ui.appslist.lazylist.models.AppListItem
import com.arttttt.appslist.impl.ui.appslist.lazylist.models.DividerListItem
import com.arttttt.appslist.impl.ui.appslist.lazylist.models.ProgressListItem
import com.arttttt.appslist.impl.ui.appslist.lazylist.ui.AppItemContent
import com.arttttt.appslist.impl.ui.appslist.lazylist.ui.DividerItemContent
import com.arttttt.appslist.impl.ui.appslist.lazylist.ui.ProgressItemContent
import com.arttttt.core.arch.content.ComponentContent
import com.arttttt.core.arch.content.ComponentContentOwner
import com.arttttt.lazylist.ListItem
import com.arttttt.uikit.theme.AppTheme
import kotlinx.collections.immutable.ImmutableList

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
                onAppClicked = component::onAppClicked,
                onStartAppsClicked = component::startApps,
                onAppCheckedChange = component::onAppCheckedChange,
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
        isStartButtonVisible: Boolean,
        onAppClicked: (String) -> Unit,
        onAppCheckedChange: (String) -> Unit,
        onStartAppsClicked: () -> Unit,
    ) {
        var parentCoordinates: LayoutCoordinates? by remember {
            mutableStateOf(null)
        }

        val currentIsStartButtonVisible by rememberUpdatedState(isStartButtonVisible)

        Box(
            modifier = Modifier
                .fillMaxSize()
                .onGloballyPositioned { coordinates ->
                    parentCoordinates = coordinates
                }
        ) {
            val transitionState = remember {
                MutableTransitionState(isStartButtonVisible)
            }

            val nestedScrollConnection = remember {
                object : NestedScrollConnection {
                    override fun onPreScroll(
                        available: Offset,
                        source: NestedScrollSource,
                    ): Offset {
                        if (currentIsStartButtonVisible && available.y != 0f) {
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
                onAppCheckedChange = onAppCheckedChange,
                onAppClicked = onAppClicked,
            )

            AnimatedVisibility(
                modifier = Modifier.align(Alignment.BottomStart),
                enter = slideInVertically { it } + fadeIn(),
                exit = slideOutVertically { it } + fadeOut(),
                visibleState = transitionState,
            ) {
                ActionsRow(
                    modifier = Modifier,
                    isStartAppsButtonVisible = isStartButtonVisible,
                    onStartAppsClicked = onStartAppsClicked,
                )
            }

            LaunchedEffect(isStartButtonVisible) {
                transitionState.targetState = isStartButtonVisible
            }
        }
    }

    @Composable
    private fun AppsList(
        modifier: Modifier,
        apps: ImmutableList<ListItem>,
        onAppClicked: (String) -> Unit,
        onAppCheckedChange: (String) -> Unit,
    ) {
        LazyColumn(
            modifier = modifier,
            contentPadding = remember {
                PaddingValues(vertical = 8.dp)
            }
        ) {
            items(
                items = apps,
                key = ListItem::key,
                contentType = { item -> item::class },
            ) { item ->
                when (item) {
                    is AppListItem -> AppItemContent(
                        modifier = Modifier.fillParentMaxWidth(),
                        item = item,
                        onClick = onAppClicked,
                        onCheckedChange = {
                            onAppCheckedChange(item.pkg)
                        },
                    )
                    is DividerListItem -> DividerItemContent(
                        modifier = Modifier.fillParentMaxWidth(),
                    )
                    is ProgressListItem -> ProgressItemContent(
                        modifier = Modifier.fillParentMaxSize(),
                    )
                    else -> throw IllegalStateException("unsupported item type: $item")
                }
            }
        }
    }

    @Composable
    private fun ActionsRow(
        modifier: Modifier,
        isStartAppsButtonVisible: Boolean,
        onStartAppsClicked: () -> Unit,
    ) {
        Box(
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
        ) {
            if (isStartAppsButtonVisible) {
                Button(
                    modifier = Modifier.fillMaxWidth(1f),
                    onClick = onStartAppsClicked,
                    colors = AppTheme.widgets.buttonColors,
                ) {
                    Text(text = stringResource(com.arttttt.localization.R.string.start_apps))
                }
            }
        }
    }
}