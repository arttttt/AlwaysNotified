package com.arttttt.appholder.arch.context

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.childContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigationSource
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.Lifecycle
import com.arkivanov.essenty.parcelable.Parcelable
import org.koin.core.scope.ScopeID

fun defaultAppComponentContext(
    context: ComponentContext,
    parentScopeID: ScopeID?,
): AppComponentContext {
    return DefaultAppComponentContext(
        context = context,
        parentScopeID = parentScopeID,
    )
}

fun AppComponentContext.childAppContext(
    key: String,
    lifecycle: Lifecycle? = this.lifecycle,
    parentScopeID: ScopeID? = this.parentScopeID,
): AppComponentContext {
    return DefaultAppComponentContext(
        context = childContext(
            key = key,
            lifecycle = lifecycle,
        ),
        parentScopeID = parentScopeID,
    )
}

inline fun <reified C : Parcelable, T : Any> AppComponentContext.customChildStack(
    parentScopeID: ScopeID?,
    source: StackNavigationSource<C>,
    initialConfiguration: C,
    key: String = "DefaultChildStack",
    persistent: Boolean = true,
    handleBackButton: Boolean = false,
    noinline childFactory: (configuration: C, AppComponentContext) -> T
): Value<ChildStack<C, T>> =
    childStack(
        source = source,
        initialStack = { listOf(initialConfiguration) },
        configurationClass = C::class,
        key = key,
        persistent = persistent,
        handleBackButton = handleBackButton,
        childFactory = { config, context ->
            val context = if (context is AppComponentContext) {
                context
            } else {
                defaultAppComponentContext(
                    context = context,
                    parentScopeID = parentScopeID,
                )
            }

            childFactory.invoke(config, context)
        },
    )