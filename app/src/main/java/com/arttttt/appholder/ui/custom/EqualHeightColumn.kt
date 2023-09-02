package com.arttttt.appholder.ui.custom

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.MeasurePolicy
import androidx.compose.ui.unit.Dp

@Composable
fun EqualHeightColumn(
    modifier: Modifier = Modifier,
    spacing: Dp,
    content: @Composable () -> Unit
) {
    val measurePolicy = rememberEqualHeightColumnMeasurePolicy(spacing)

    Layout(
        content = content,
        modifier = modifier,
        measurePolicy = measurePolicy,
    )
}

@Composable
private fun rememberEqualHeightColumnMeasurePolicy(
    spacing: Dp,
): MeasurePolicy {
    return remember {
        MeasurePolicy { measurables, constraints ->
            val maxHeight = measurables.maxOf { measurable ->
                measurable.minIntrinsicHeight(constraints.maxWidth)
            }

            val updatedConstraints = constraints.copy(
                minHeight = maxHeight,
                maxHeight = maxHeight,
            )
            val placeables = measurables.map { it.measure(updatedConstraints) }

            layout(constraints.maxWidth, constraints.maxHeight) {
                var yPosition = 0
                placeables.forEach {
                    it.placeRelative(0, yPosition)
                    yPosition += maxHeight + spacing.roundToPx()
                }
            }
        }
    }
}