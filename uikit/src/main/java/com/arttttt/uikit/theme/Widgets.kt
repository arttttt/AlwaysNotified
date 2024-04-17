package com.arttttt.uikit.theme

import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SwitchColors
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable

@Immutable
object Widgets {
    val switchColors: SwitchColors
        @Composable
        get() = SwitchDefaults.colors(
            checkedTrackColor = AppTheme.colors.textAndIcons,
            checkedThumbColor = AppTheme.colors.primary,
            uncheckedBorderColor = AppTheme.colors.textAndIcons,
            uncheckedTrackColor = AppTheme.colors.textAndIcons,
            uncheckedThumbColor = AppTheme.colors.primary,
        )

    @OptIn(ExperimentalMaterial3Api::class)
    val topAppBarColors: TopAppBarColors
        @Composable
        get() = TopAppBarDefaults.topAppBarColors(
            containerColor = AppTheme.colors.secondary,
            titleContentColor = AppTheme.colors.textAndIcons,
            actionIconContentColor = AppTheme.colors.textAndIcons,
        )

    val buttonColors: ButtonColors
        @Composable
        get() = ButtonDefaults.buttonColors(
            containerColor = AppTheme.colors.primary
        )
}