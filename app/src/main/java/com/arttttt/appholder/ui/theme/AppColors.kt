package com.arttttt.appholder.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Immutable
data class AppColors(
    val primary: Color = colorDarkPrimary,
    val secondary: Color = colorDarkSecondary,
    val tertiary: Color = colorDartTertiary,
    val textAndIcons: Color = colorDarkTextAndIcons,
    val divider: Color = colorDarkDivider,
)

val LocalAppColors = staticCompositionLocalOf { AppColors() }