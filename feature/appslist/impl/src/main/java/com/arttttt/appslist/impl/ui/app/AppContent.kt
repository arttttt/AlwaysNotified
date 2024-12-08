package com.arttttt.appslist.impl.ui.app

import android.graphics.drawable.Drawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.HapticFeedbackConstantsCompat
import com.arttttt.appslist.impl.components.app.AppComponent
import com.arttttt.appslist.impl.ui.app.lazylist.delegates.ActivityItemContent
import com.arttttt.appslist.impl.ui.app.lazylist.models.ActivityListItem
import com.arttttt.core.arch.content.ComponentContent
import com.arttttt.core.arch.dialog.DismissEvent
import com.arttttt.lazylist.ListItem
import com.arttttt.uikit.LocalCorrectHapticFeedback
import com.arttttt.uikit.theme.AppTheme
import com.google.accompanist.drawablepainter.rememberDrawablePainter

internal class AppContent(
    private val component: AppComponent
) : ComponentContent {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content(modifier: Modifier) {
        val uiState by component.uiStates.collectAsState()

        ModalBottomSheet(
            onDismissRequest = {
                component.onDismiss(DismissEvent)
            },
            sheetState = rememberModalBottomSheetState(
                skipPartiallyExpanded = true,
            ),
            contentWindowInsets = { WindowInsets(0, 0, 0 , 0) },
            containerColor = AppTheme.colors.secondary,
            shape = AppTheme.shapes.roundedCorners.medium(
                bottomStart = 0.dp,
                bottomEnd = 0.dp,
            ),
        ) {
            Column(
                modifier = modifier.navigationBarsPadding()
            ) {
                AppTitle(
                    title = uiState.title,
                    icon = uiState.icon,
                )

                ActivitiesList(
                    modifier = Modifier.weight(
                        weight = 1f,
                        fill = false,
                    ),
                    items = uiState.items,
                    onActivityClicked = component::onActivityClicked,
                )
            }
        }
    }

    @Composable
    private fun AppTitle(
        title: String,
        icon: Drawable?,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 16.dp,
                    vertical = 8.dp,
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            if (icon != null) {
                Image(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape),
                    painter = rememberDrawablePainter(icon),
                    contentDescription = null,
                )

                Spacer(modifier = Modifier.width(16.dp))
            }

            Text(
                text = title,
                fontSize = 18.sp,
            )
        }
    }

    @Composable
    private fun ActivitiesList(
        modifier: Modifier,
        items: List<ListItem>,
        onActivityClicked: (String) -> Unit,
    ) {
        val hapticFeedback = LocalCorrectHapticFeedback.current

        LazyColumn(
            modifier = modifier
                .navigationBarsPadding()
                .fillMaxWidth(),
            contentPadding = remember {
                PaddingValues(
                    bottom = 8.dp,
                )
            }
        ) {
            items(
                items = items,
                key = { item -> item.key },
                contentType = { item -> item::class }
            ) { item ->
                when (item) {
                    is ActivityListItem -> ActivityItemContent(
                        modifier = Modifier.fillParentMaxWidth(),
                        item = item,
                        onClick = { name ->
                            hapticFeedback.performHapticFeedback(HapticFeedbackConstantsCompat.VIRTUAL_KEY)
                            onActivityClicked(name)
                        },
                    )
                }
            }
        }
    }
}