package com.arttttt.appholder.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp

@Immutable
data class Typography(
    val topBar: TextStyle = TextStyle(
        fontSize = 22.sp
    )
)

val LocalTypography = staticCompositionLocalOf { Typography() }