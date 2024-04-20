package com.arttttt.appslist.impl.ui.app

import android.util.Log
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.view.HapticFeedbackConstantsCompat
import com.arttttt.appslist.impl.components.app.AppComponent
import com.arttttt.appslist.impl.ui.appslist.lazylist.delegates.ActivityListDelegate
import com.arttttt.core.arch.content.ComponentContent
import com.arttttt.core.arch.dialog.DismissEvent
import com.arttttt.lazylist.ListItem
import com.arttttt.lazylist.dsl.rememberLazyListDelegateManager
import com.arttttt.uikit.LocalCorrectHapticFeedback
import com.arttttt.uikit.theme.AppTheme
import kotlinx.collections.immutable.persistentListOf

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
                skipPartiallyExpanded = false,
            ),
            windowInsets = remember { WindowInsets(0, 0, 0 , 0) },
            containerColor = AppTheme.colors.secondary,
            shape = AppTheme.shapes.roundedCorners.medium(
                bottomStart = 0.dp,
                bottomEnd = 0.dp,
            ),
        ) {
            SheetContent(
                items = uiState.items,
                onActivityClicked = component::onActivityClicked,
            )
        }
    }

    @Composable
    private fun SheetContent(
        items: List<ListItem>,
        onActivityClicked: (String) -> Unit,
    ) {
        val hapticFeedback = LocalCorrectHapticFeedback.current

        val lazyListDelegateManager = rememberLazyListDelegateManager(
            delegates = persistentListOf(
                ActivityListDelegate(
                    onClick = { name ->
                        hapticFeedback.performHapticFeedback(HapticFeedbackConstantsCompat.VIRTUAL_KEY)
                        onActivityClicked(name)
                    },
                ),
            ),
        )


        LazyColumn(
            modifier = Modifier
                .navigationBarsPadding()
                .fillMaxWidth(),
            contentPadding = remember {
                PaddingValues(vertical = 8.dp)
            }
        ) {
            items(
                items = items,
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
}