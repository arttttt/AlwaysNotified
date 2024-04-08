package com.arttttt.alwaysnotified.ui.appssearch

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text2.BasicTextField2
import androidx.compose.foundation.text2.input.clearText
import androidx.compose.foundation.text2.input.rememberTextFieldState
import androidx.compose.foundation.text2.input.textAsFlow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arttttt.alwaysnotified.R
import com.arttttt.alwaysnotified.components.appssearch.AppsSearchComponent
import com.arttttt.alwaysnotified.ui.theme.AppTheme
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AppsSearchContent(
    component: AppsSearchComponent,
) {
    val uiState by component.uiState.collectAsState()

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

        val textState = rememberTextFieldState()

        BasicTextField2(
            modifier = Modifier.weight(1f),
            state = textState,
            textStyle = TextStyle.Default.copy(
                fontSize = 16.sp,
                color = AppTheme.colors.textAndIcons,
            ),
            cursorBrush = SolidColor(AppTheme.colors.textAndIcons),
            decorator = { innerTextField ->
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
                        if (uiState.text.isEmpty()) {
                            Text(
                                text = stringResource(
                                    id = R.string.search_hint,
                                ),
                                color = AppTheme.colors.textAndIcons.copy(
                                    alpha = 0.3f,
                                )
                            )
                        }

                        innerTextField.invoke()
                    }

                    if (uiState.text.isNotEmpty()) {
                        IconButton(
                            onClick = {
                                textState.clearText()
                            },
                        ) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = null,
                            )
                        }
                    }
                }
            }
        )

        LaunchedEffect(
            component,
            textState,
        ) {
            textState
                .textAsFlow()
                .onEach { text ->
                    component.onTextChanged(text.toString())
                }
                .launchIn(this)
        }
    }
}