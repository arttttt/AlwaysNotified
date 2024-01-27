package com.arttttt.alwaysnotified.ui.custom

import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.platform.LocalView

interface HapticFeedback {
    fun performHapticFeedback(hapticFeedbackConstant: Int)
}

@Composable
fun rememberHapticFeedback(): HapticFeedback {
    val view = LocalView.current

    return remember(view) {
        object : HapticFeedback {
            override fun performHapticFeedback(hapticFeedbackConstant: Int) {
                view.performHapticFeedback(hapticFeedbackConstant)
            }
        }
    }
}

val LocalCorrectHapticFeedback = staticCompositionLocalOf<HapticFeedback> {
    error("HapticFeedbackLocal not present")
}

fun Modifier.hapticFeedbackClickable(
    hapticsFeedbackConstant: Int,
    block: () -> Unit,
): Modifier = composed {
    val hapticsFeedback = LocalCorrectHapticFeedback.current

    then(
        clickable(
            onClick = remember(hapticsFeedbackConstant, block) {
                {
                    hapticsFeedback.performHapticFeedback(hapticsFeedbackConstant)
                    block.invoke()
                }
            }
        )
    )
}