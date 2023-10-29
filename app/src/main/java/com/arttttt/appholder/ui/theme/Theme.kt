package com.arttttt.appholder.ui.theme

import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable

object AppTheme {

    val colors: AppColors
        @Composable
        @ReadOnlyComposable
        get() = LocalAppColors.current

    val widgets: Widgets
        @Composable
        @ReadOnlyComposable
        get() = Widgets

    val shapes: Shapes
        @Composable
        @ReadOnlyComposable
        get() = ShapesHolder
}

@Composable
fun AppTheme(
    content: @Composable () -> Unit
) {
  MaterialTheme(
    content = {
        CompositionLocalProvider(
            LocalAppColors provides AppTheme.colors,
            LocalContentColor provides AppTheme.colors.textAndIcons,
        ) {
            content.invoke()
        }
    }
  )
}