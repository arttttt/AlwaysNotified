package com.arttttt.appssearch.impl.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.input.TextFieldDecorator
import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arttttt.uikit.theme.AppTheme
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@Composable
internal fun SearchInput(
    text: String,
    onTextChanged: (String) -> Unit,
) {
    val currentOnTextChanged by rememberUpdatedState(onTextChanged)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {

        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = null,
        )

        Spacer(
            modifier = Modifier.width(8.dp),
        )

        val textState = rememberTextFieldState(
            initialText = text,
        )

        BasicTextField(
            modifier = Modifier.weight(1f),
            state = textState,
            textStyle = TextStyle.Default.copy(
                fontSize = 16.sp,
                color = AppTheme.colors.textAndIcons,
            ),
            cursorBrush = SolidColor(AppTheme.colors.textAndIcons),
            decorator = searchInputDecorator(
                text = text,
                onClearText = {
                    textState.clearText()
                },
            )
        )

        LaunchedEffect(textState) {
            snapshotFlow { textState.text }
                .onEach { text ->
                    currentOnTextChanged(text.toString())
                }
                .launchIn(this)
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun searchInputDecorator(
    text: String,
    onClearText: () -> Unit,
): TextFieldDecorator {
    return TextFieldDecorator { innerTextField ->
        Row(
            modifier = Modifier.fillMaxHeight(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f),
                contentAlignment = Alignment.CenterStart,
            ) {
                if (text.isEmpty()) {
                    Text(
                        text = stringResource(
                            id = com.arttttt.localization.R.string.search_hint,
                        ),
                        color = AppTheme.colors.textAndIcons.copy(
                            alpha = 0.3f,
                        )
                    )
                }

                innerTextField.invoke()
            }

            if (text.isNotEmpty()) {
                IconButton(
                    onClick = onClearText,
                ) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = null,
                    )
                }
            }
        }
    }
}