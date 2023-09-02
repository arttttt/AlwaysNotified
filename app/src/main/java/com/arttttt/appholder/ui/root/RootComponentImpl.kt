package com.arttttt.appholder.ui.root

import android.os.Parcelable
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.replaceCurrent
import com.arkivanov.decompose.value.Value
import com.arttttt.appholder.arch.DecomposeComponent
import com.arttttt.appholder.arch.asStateFlow
import com.arttttt.appholder.arch.context.AppComponentContext
import com.arttttt.appholder.arch.context.childAppContext
import com.arttttt.appholder.arch.context.customChildStack
import com.arttttt.appholder.arch.stackComponentEvents
import com.arttttt.appholder.koinScope
import com.arttttt.appholder.ui.appslist.AppsListComponentImpl
import com.arttttt.appholder.ui.permissions.PermissionsComponent
import com.arttttt.appholder.ui.permissions.PermissionsComponentImpl
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.plus
import kotlinx.parcelize.Parcelize

class RootComponentImpl(
    componentContext: AppComponentContext,
) : RootComponent,
    AppComponentContext by componentContext {

    private sealed class Config : Parcelable {

        @Parcelize
        data object AppsList : Config()

        @Parcelize
        data object Permissions : Config()
    }

    private val scope = koinScope()

    private val coroutineScope = MainScope() + SupervisorJob()

    private val navigation = StackNavigation<Config>()

    private val permissionsComponent: PermissionsComponent = PermissionsComponentImpl(
        componentContext = childAppContext(
            key = PermissionsComponent::class.java.name,
        )
    )

    override val stack: Value<ChildStack<*, DecomposeComponent>> = customChildStack(
        parentScopeID = scope.id,
        source = navigation,
        initialConfiguration = initialConfiguration(),
        handleBackButton = true,
        childFactory = ::createComponent
    )

    init {
        stack
            .stackComponentEvents<PermissionsComponent.Event.AllPermissionsGranted>()
            .take(1)
            .onEach {
                navigation.replaceCurrent(Config.AppsList)
            }
            .launchIn(coroutineScope)
    }

    private fun createComponent(
        config: Config,
        context: AppComponentContext,
    ): DecomposeComponent {
        return when (config) {
            is Config.AppsList -> AppsListComponentImpl(
                componentContext = context,
            )
            is Config.Permissions -> PermissionsComponentImpl(
                componentContext = context,
            )
        }
    }

    private fun initialConfiguration(): Config {
        return if (permissionsComponent.needRequestPermissions()) {
            Config.Permissions
        } else {
            Config.AppsList
        }
    }
}