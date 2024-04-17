package com.arttttt.uikit.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

interface Shapes {

    val roundedCorners: ShapesImpl.RoundedCorners
}

object ShapesHolder : Shapes {

    override val roundedCorners: ShapesImpl.RoundedCorners = ShapesImpl.RoundedCorners
}

sealed class ShapesImpl {

    private val tiny = 4.dp
    private val small = 8.dp
    private val medium = 16.dp
    private val huge = 24.dp

    protected abstract fun createShape(
        topStart: Dp,
        topEnd: Dp,
        bottomEnd: Dp,
        bottomStart: Dp,
    ): Shape

    fun tiny(
        topStart: Dp = tiny,
        topEnd: Dp = tiny,
        bottomEnd: Dp = tiny,
        bottomStart: Dp = tiny,
    ): Shape{
        return createShape(topStart, topEnd, bottomEnd, bottomStart)
    }

    fun small(
        topStart: Dp = small,
        topEnd: Dp = small,
        bottomEnd: Dp = small,
        bottomStart: Dp = small,
    ): Shape{
        return createShape(topStart, topEnd, bottomEnd, bottomStart)
    }

    fun medium(
        topStart: Dp = medium,
        topEnd: Dp = medium,
        bottomEnd: Dp = medium,
        bottomStart: Dp = medium,
    ): Shape{
        return createShape(topStart, topEnd, bottomEnd, bottomStart)
    }

    fun huge(
        topStart: Dp = huge,
        topEnd: Dp = huge,
        bottomEnd: Dp = huge,
        bottomStart: Dp = huge,
    ): Shape{
        return createShape(topStart, topEnd, bottomEnd, bottomStart)
    }

    data object RoundedCorners : ShapesImpl() {

        override fun createShape(topStart: Dp, topEnd: Dp, bottomEnd: Dp, bottomStart: Dp): Shape {
            return RoundedCornerShape(topStart, topEnd, bottomEnd, bottomStart)
        }
    }
}