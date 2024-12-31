package com.arttttt.appslist.impl.ui

import androidx.compose.animation.AnimatedVisibility
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
import com.arttttt.appslist.impl.components.InternalAppsListComponent
import com.arttttt.appslist.impl.ui.lazylist.models.AppListItem
import com.arttttt.appslist.impl.ui.lazylist.models.DividerListItem
import com.arttttt.appslist.impl.ui.lazylist.models.ProgressListItem
import com.arttttt.appslist.impl.ui.lazylist.models.UnsupportedAppListItem
import com.arttttt.appslist.impl.ui.lazylist.ui.AppItemContent
import com.arttttt.appslist.impl.ui.lazylist.ui.DividerItemContent
import com.arttttt.appslist.impl.ui.lazylist.ui.ProgressItemContent
import com.arttttt.appslist.impl.ui.lazylist.ui.UnsupportedAppItemContent
import com.arttttt.core.arch.content.ComponentContent
import com.arttttt.core.arch.content.ComponentContentOwner
import com.arttttt.lazylist.ListItem
import com.arttttt.localization.R
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
                onStartAppsClicked = component.startApps,
                onAppCheckedChange = component::onAppClicked,
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
            var transitionState by remember(currentIsStartButtonVisible) {
                mutableStateOf(currentIsStartButtonVisible)
            }

            val nestedScrollConnection = remember {
                object : NestedScrollConnection {
                    override fun onPreScroll(
                        available: Offset,
                        source: NestedScrollSource,
                    ): Offset {
                        if (currentIsStartButtonVisible && available.y != 0f) {
                            transitionState = available.y > 0
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
                visible = transitionState,
                modifier = Modifier.align(Alignment.BottomStart),
                enter = slideInVertically { it } + fadeIn(),
                exit = slideOutVertically { it } + fadeOut(),
            ) {
                ActionsRow(
                    modifier = Modifier,
                    isStartAppsButtonVisible = isStartButtonVisible,
                    onStartAppsClicked = onStartAppsClicked,
                )
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
                    is UnsupportedAppListItem -> UnsupportedAppItemContent(
                        modifier = Modifier.fillParentMaxWidth(),
                        item = item,
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
                    Text(text = stringResource(R.string.start_apps))
                }
            }
        }
    }
}