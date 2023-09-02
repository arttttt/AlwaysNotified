package com.arttttt.appholder.ui.root

import android.os.Parcelable
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.value.Value
import com.arttttt.appholder.AppsLauncher
import com.arttttt.appholder.ui.appslist.AppsListComponentImpl
import com.arttttt.appholder.arch.DecomposeComponent
import kotlinx.parcelize.Parcelize
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class RootComponentImpl(
    componentContext: ComponentContext,
) : RootComponent,
    ComponentContext by componentContext,
    KoinComponent {

    private sealed class Config : Parcelable {

        @Parcelize
        data object AppsList : Config()
    }

    private val appsLauncher: AppsLauncher by inject()

    private val navigation = StackNavigation<Config>()

    override val stack: Value<ChildStack<*, DecomposeComponent>> = childStack(
        source = navigation,
        initialConfiguration = Config.AppsList,
        handleBackButton = true,
        childFactory = ::createComponent
    )

    private fun createComponent(
        config: Config,
        context: ComponentContext,
    ): DecomposeComponent {
        return when (config) {
            is Config.AppsList -> AppsListComponentImpl(
                componentContext = context,
                appsLauncher = appsLauncher,
            )
        }
    }
}