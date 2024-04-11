package com.arttttt.alwaysnotified.components.root

import com.arkivanov.decompose.childContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.router.stack.replaceCurrent
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import com.arttttt.core.arch.context.AppComponentContext
import com.arttttt.appslist.api.AppsListComponent
import com.arttttt.alwaysnotified.components.permissions.PermissionsComponent
import com.arttttt.alwaysnotified.components.permissions.PermissionsComponentImpl
import com.arttttt.alwaysnotified.components.settings.SettingsComponentImpl
import com.arttttt.core.arch.koinScope
import com.arttttt.core.arch.DecomposeComponent
import com.arttttt.core.arch.context.wrapComponentContext
import com.arttttt.core.arch.stackComponentEvents
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.take
import kotlinx.serialization.Serializable
import org.koin.core.component.getScopeId
import org.koin.core.qualifier.qualifier

class RootComponentImpl(
    componentContext: AppComponentContext,
) : RootComponent,
    AppComponentContext by componentContext {

    @Serializable
    private sealed class Config {

        @Serializable
        data object AppsList : Config()

        @Serializable
        data object Permissions : Config()

        @Serializable
        data object Settings : Config()
    }

    private val coroutineScope = coroutineScope()
    private val koinScope = koinScope(
        scopeID = getScopeId(),
        qualifier = qualifier<RootComponent>(),
    )

    private val appsListComponentFactory: AppsListComponent.Factory by koinScope.inject()

    private val navigation = StackNavigation<Config>()

    private val permissionsComponent: PermissionsComponent = PermissionsComponentImpl(
        componentContext = wrapComponentContext(
            context = childContext(
                key = PermissionsComponent::class.java.name,
            ),
            parentScopeID = koinScope.id,
        )
    )

    override val stack: Value<ChildStack<*, DecomposeComponent>> = childStack(
        serializer = Config.serializer(),
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
            .stackComponentEvents<AppsListComponent.Event>()
            .filterIsInstance<AppsListComponent.Event.OpenSettings>()
            .onEach {
                navigation.push(Config.Settings)
            }
            .launchIn(coroutineScope)
    }

    private fun createComponent(
        config: Config,
        context: AppComponentContext,
    ): DecomposeComponent {
        val wrappedContext = wrapComponentContext(
            context = context,
            parentScopeID = koinScope.id,
        )

        return when (config) {
            is Config.AppsList -> appsListComponentFactory.create(
                context = wrappedContext,
            )
            is Config.Permissions -> permissionsComponent
            is Config.Settings -> SettingsComponentImpl(
                componentContext = wrappedContext,
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