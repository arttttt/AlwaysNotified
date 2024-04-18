package com.arttttt.appslist.impl.ui.app

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arttttt.appslist.impl.components.app.AppComponent
import com.arttttt.core.arch.content.ComponentContent
import com.arttttt.core.arch.dialog.DismissEvent
import com.arttttt.uikit.theme.AppTheme

internal class AppContent(
    private val component: AppComponent
) : ComponentContent {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content(modifier: Modifier) {
        ModalBottomSheet(
            onDismissRequest = {
                component.onDismiss(DismissEvent)
            },
            sheetState = rememberModalBottomSheetState(
                skipPartiallyExpanded = true,
            ),
            windowInsets = remember { WindowInsets(0, 0, 0 , 0) },
            containerColor = AppTheme.colors.secondary,
            shape = AppTheme.shapes.roundedCorners.medium(
                bottomStart = 0.dp,
                bottomEnd = 0.dp,
            ),
        ) {
            Box(
                modifier = Modifier
                    .navigationBarsPadding()
                    .fillMaxWidth()
                    .height(300.dp)
            )
        }
    }
}