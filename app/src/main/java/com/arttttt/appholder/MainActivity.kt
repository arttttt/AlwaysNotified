package com.arttttt.appholder

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.CompositionLocalProvider
import androidx.core.view.WindowCompat
import com.arkivanov.decompose.defaultComponentContext
import com.arttttt.appholder.arch.context.defaultAppComponentContext
import com.arttttt.appholder.components.root.RootComponent
import com.arttttt.appholder.components.root.RootComponentImpl
import com.arttttt.appholder.di.mainActivityModule
import com.arttttt.appholder.ui.custom.LocalCorrectHapticFeedback
import com.arttttt.appholder.ui.custom.rememberHapticFeedback
import com.arttttt.appholder.ui.root.RootContent
import com.arttttt.appholder.ui.theme.AppTheme
import com.arttttt.appholder.utils.extensions.unsafeCastTo
import org.koin.android.scope.AndroidScopeComponent
import org.koin.androidx.scope.activityScope
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules
import org.koin.core.scope.Scope
import org.koin.core.scope.ScopeCallback


/**
 * todo: remove [AndroidScopeComponent]
 */
class MainActivity : ComponentActivity(), AndroidScopeComponent {

    override val scope by activityScope()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        loadKoinModules(mainActivityModule)
        scope.registerCallback(
            object : ScopeCallback {
                override fun onScopeClose(scope: Scope) {
                    unloadKoinModules(mainActivityModule)
                }
            }
        )
        scope.declare(this.unsafeCastTo<ComponentActivity>())

        val rootComponent: RootComponent = RootComponentImpl(
            componentContext = defaultAppComponentContext(
                context = defaultComponentContext(),
                parentScopeID = scope.id,
            ),
        )

        setContent {
            CompositionLocalProvider(LocalCorrectHapticFeedback provides rememberHapticFeedback()) {
                AppTheme {
                    RootContent(
                        component = rootComponent,
                    )
                }
            }
        }
    }
}