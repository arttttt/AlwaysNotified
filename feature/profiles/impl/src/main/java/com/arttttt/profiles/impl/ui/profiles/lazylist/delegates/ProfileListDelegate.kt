package com.arttttt.profiles.impl.ui.profiles.lazylist.delegates

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Text
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.arttttt.core.arch.base.dsl.lazyListDelegate
import com.arttttt.profiles.impl.ui.profiles.lazylist.models.ProfileListItem
import com.arttttt.uikit.theme.AppTheme

@OptIn(ExperimentalFoundationApi::class)
fun ProfileListDelegate(
    onClick: (String) -> Unit,
    longClick: (String) -> Unit,
) = lazyListDelegate<ProfileListItem> {

    val colors = AppTheme.colors

    val innerCircleRadiusSelected = remember {
        20.dp
    }

    val innerCircleRadius = remember {
        24.dp
    }

    val width = remember {
        48.dp
    }

    val interactionSource = remember {
        MutableInteractionSource()
    }

    Column(
        modifier = Modifier
            .widthIn(
                max = width,
            )
            .combinedClickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = {
                    onClick.invoke(item.id)
                },
                onLongClick = {
                    longClick.invoke(item.id)
                },
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(
            modifier = Modifier
                .size(width)
                .clip(CircleShape)
                .combinedClickable(
                    interactionSource = interactionSource,
                    indication = rememberRipple(),
                    onClick = {
                        onClick.invoke(item.id)
                    },
                    onLongClick = {
                        longClick.invoke(item.id)
                    },
                )
                .drawWithCache {
                    onDrawWithContent {
                        drawContent()

                        drawCircle(
                            color = Color(item.color),
                            radius = if (item.isSelected) {
                                innerCircleRadiusSelected.toPx()
                            } else {
                                innerCircleRadius.toPx()
                            }
                        )

                        if (item.isSelected) {
                            drawCircle(
                                color = colors.textAndIcons,
                                style = Stroke(
                                    width = 4.dp.toPx()
                                )
                            )
                        }
                    }
                }
        )

        Text(
            text = item.title,
            color = AppTheme.colors.textAndIcons,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}