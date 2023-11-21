package com.arttttt.appholder.components.root

import android.os.Parcelable
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.router.stack.replaceCurrent
import com.arkivanov.decompose.value.Value
import com.arttttt.appholder.arch.shared.DecomposeComponent
import com.arttttt.appholder.arch.shared.context.AppComponentContext
import com.arttttt.appholder.arch.shared.context.childAppContext
import com.arttttt.appholder.arch.shared.context.customChildStack
import com.arttttt.appholder.arch.shared.stackComponentEvents
import com.arttttt.appholder.components.appslist.AppListComponent
import com.arttttt.appholder.components.appslist.AppsListComponentImpl
import com.arttttt.appholder.components.permissions.PermissionsComponent
import com.arttttt.appholder.components.permissions.PermissionsComponentImpl
import com.arttttt.appholder.components.settings.SettingsComponentImpl
import com.arttttt.appholder.utils.extensions.koinScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.filterIsInstance
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

        @Parcelize
        data object Settings : Config()
    }

    private val scope = koinScope()

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
            .stackComponentEvents<PermissionsComponent.Event>()
            .filterIsInstance<PermissionsComponent.Event.AllPermissionsGranted>()
            .take(1)
            .onEach {
                navigation.replaceCurrent(Config.AppsList)
            }
            .launchIn(coroutineScope)

        stack
            .stackComponentEvents<AppListComponent.Event>()
            .filterIsInstance<AppListComponent.Event.OpenSettings>()
            .onEach {
                navigation.push(Config.Settings)
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
                resourcesProvider = scope.get(),
            )
            is Config.Permissions -> PermissionsComponentImpl(
                componentContext = context,
            )
            is Config.Settings -> SettingsComponentImpl(
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