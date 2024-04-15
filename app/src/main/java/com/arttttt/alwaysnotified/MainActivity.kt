package com.arttttt.alwaysnotified

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.toArgb
import androidx.core.view.WindowCompat
import com.arkivanov.decompose.defaultComponentContext
import com.arttttt.alwaysnotified.components.root.RootComponent
import com.arttttt.alwaysnotified.components.root.RootComponentImpl
import com.arttttt.alwaysnotified.ui.root.RootContent
import com.arttttt.alwaysnotified.utils.unsafeCastTo
import com.arttttt.core.arch.context.wrapComponentContext
import com.arttttt.uikit.LocalCorrectHapticFeedback
import com.arttttt.uikit.rememberHapticFeedback
import com.arttttt.uikit.theme.AppTheme
import org.koin.android.scope.AndroidScopeComponent
import org.koin.androidx.scope.activityScope

/**
 * todo: remove [AndroidScopeComponent]
 */
class MainActivity : ComponentActivity(), AndroidScopeComponent {

    override val scope by activityScope()

    @OptIn(ExperimentalFoundationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        scope.declare(this.unsafeCastTo<ComponentActivity>())

        val rootComponent: RootComponent = RootComponentImpl(
            componentContext = wrapComponentContext(
                context = defaultComponentContext(),
                parentScopeID = scope.id,
            ),
        )

        setContent {
            val colors = AppTheme.colors

            LaunchedEffect(Unit) {
                window.statusBarColor = colors.secondary.toArgb()
                WindowCompat
                    .getInsetsController(window, window.decorView)
                    .isAppearanceLightStatusBars = false
            }

            CompositionLocalProvider(
                LocalCorrectHapticFeedback provides rememberHapticFeedback(),
                LocalOverscrollConfiguration provides null
            ) {
                AppTheme {
                    RootContent(
                        component = rootComponent,
                    )
                }
            }
        }
    }
}