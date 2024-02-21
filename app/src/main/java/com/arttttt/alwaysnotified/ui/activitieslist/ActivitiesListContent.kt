package com.arttttt.alwaysnotified.ui.activitieslist

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arttttt.alwaysnotified.components.activitieslist.ActivitiesListComponent
import com.arttttt.alwaysnotified.ui.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActivitiesListContent(
    component: ActivitiesListComponent
) {
    ModalBottomSheet(
        onDismissRequest = {

        },
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