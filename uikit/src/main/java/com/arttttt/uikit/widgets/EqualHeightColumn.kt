package com.arttttt.uikit.widgets

import androidx.compose.foundation.layout.LayoutScopeMarker
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.MeasurePolicy
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.node.ParentDataModifierNode
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp

@Composable
fun EqualHeightColumn(
    modifier: Modifier = Modifier,
    spacing: Dp,
    content: @Composable EqualHeightColumnScope.() -> Unit
) {
    val measurePolicy = rememberEqualHeightColumnMeasurePolicy(spacing)

    Layout(
        content = { EqualHeightColumnScopeInstance.content() },
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
            val placeables = measurables.map { measurable ->
                measurable.measure(
                    if (measurable.equalHeightColumnChildDataNode?.ignoreEqualHeight == true) {
                        Constraints.fixed(
                            width = constraints.maxWidth,
                            height = measurable.minIntrinsicHeight(constraints.maxWidth),
                        )
                    } else {
                        updatedConstraints
                    }
                )
            }

            layout(constraints.maxWidth, constraints.maxHeight) {
                var yPosition = 0
                placeables.forEachIndexed { index, placeable ->
                    placeable.placeRelative(0, yPosition)

                    val measurable = measurables[index]

                    val height = if (measurable.equalHeightColumnChildDataNode?.ignoreEqualHeight == true) {
                        placeable.height
                    } else {
                        maxHeight
                    }

                    yPosition += height + spacing.roundToPx()
                }
            }
        }
    }
}

@LayoutScopeMarker
@Immutable
interface EqualHeightColumnScope {

    @Stable
    fun Modifier.ignoreEqualHeight(): Modifier
}

internal object EqualHeightColumnScopeInstance : EqualHeightColumnScope {

    override fun Modifier.ignoreEqualHeight(): Modifier {
        return this.then(
            EqualHeightColumnChildDataElement(
                ignoreEqualHeight = true,
            )
        )
    }
}

private class EqualHeightColumnChildDataElement(
    val ignoreEqualHeight: Boolean,
) : ModifierNodeElement<EqualHeightColumnChildDataNode>() {
    override fun create(): EqualHeightColumnChildDataNode {
        return EqualHeightColumnChildDataNode(
            ignoreEqualHeight = ignoreEqualHeight,
        )
    }

    override fun update(node: EqualHeightColumnChildDataNode) {
        node.ignoreEqualHeight = ignoreEqualHeight
    }

    override fun hashCode(): Int {
        return ignoreEqualHeight.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true

        val otherModifier = other as? EqualHeightColumnChildDataElement ?: return false

        return ignoreEqualHeight == otherModifier.ignoreEqualHeight
    }
}

private class EqualHeightColumnChildDataNode(
    var ignoreEqualHeight: Boolean,
) : ParentDataModifierNode, Modifier.Node() {

    override fun Density.modifyParentData(parentData: Any?) = this@EqualHeightColumnChildDataNode
}

private val Measurable.equalHeightColumnChildDataNode: EqualHeightColumnChildDataNode?
    get() = parentData as? EqualHeightColumnChildDataNode