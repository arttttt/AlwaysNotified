package com.arttttt.alwaysnotified.ui.profileactions

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.arttttt.alwaysnotified.components.profileactions.ProfileActionsComponent
import com.arttttt.alwaysnotified.ui.common.TextButtonWithIcon
import com.arttttt.alwaysnotified.ui.theme.AppTheme
import com.arttttt.core.arch.dialog.DismissEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileActionsContent(
    component: ProfileActionsComponent
) {
    val state by component.uiState.subscribeAsState()

    ModalBottomSheet(
        onDismissRequest = {
            component.onDismiss(DismissEvent)
        },
        windowInsets = remember { WindowInsets(0, 0, 0 , 0) },
        containerColor = AppTheme.colors.secondary,
        shape = AppTheme.shapes.roundedCorners.medium(
            bottomStart = 0.dp,
            bottomEnd = 0.dp,
        ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
        ) {
            state.items.forEach { action ->
                TextButtonWithIcon(
                    onClick = {
                        component.profileActionClicked(action)
                    },
                    text = {
                        Text(text = action.title)
                    },
                    icon = {
                        Icon(
                            imageVector = action.icon,
                            contentDescription = null,
                        )
                    }
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}