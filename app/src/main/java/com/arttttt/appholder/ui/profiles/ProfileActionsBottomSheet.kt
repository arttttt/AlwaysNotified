package com.arttttt.appholder.ui.profiles

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.arttttt.appholder.ui.common.TextButtonWithIcon
import com.arttttt.appholder.ui.theme.AppTheme
import kotlinx.collections.immutable.ImmutableSet

data class ProfileAction(
    val title: String,
    val icon: ImageVector,
    val onClick: () -> Unit,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileActionsBottomSheet(
    onDismiss: () -> Unit,
    actions: ImmutableSet<ProfileAction>,
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
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
            actions.forEach { action ->
                TextButtonWithIcon(
                    onClick = action.onClick,
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
        }
    }
}