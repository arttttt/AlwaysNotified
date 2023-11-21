package com.arttttt.appholder.arch.shared.context

import android.util.Log
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.childContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigationSource
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.Lifecycle
import com.arkivanov.essenty.lifecycle.doOnDestroy
import com.arkivanov.essenty.parcelable.Parcelable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import org.koin.core.scope.ScopeID

fun defaultAppComponentContext(
    context: ComponentContext,
    parentScopeID: ScopeID?,
): AppComponentContext {
    val coroutineScope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    context.lifecycle.doOnDestroy {
        Log.e("TEST", "coroutine scope cleared")

        coroutineScope.coroutineContext.cancelChildren()
    }

    return DefaultAppComponentContext(
        context = context,
        parentScopeID = parentScopeID,
        coroutineScope = coroutineScope,
    )
}

fun AppComponentContext.childAppContext(
    key: String,
    lifecycle: Lifecycle? = this.lifecycle,
    parentScopeID: ScopeID? = this.parentScopeID,
): AppComponentContext {
    val coroutineScope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    val context = childContext(
        key = key,
        lifecycle = lifecycle,
    )

    context.lifecycle.doOnDestroy {
        Log.e("TEST", "coroutine scope cleared")

        coroutineScope.coroutineContext.cancelChildren()
    }

    return DefaultAppComponentContext(
        context = context,
        parentScopeID = parentScopeID,
        coroutineScope = coroutineScope,
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
            childFactory.invoke(
                config,
                if (context is AppComponentContext) {
                    context
                } else {
                    defaultAppComponentContext(
                        context = context,
                        parentScopeID = parentScopeID,
                    )
                },
            )
        },
    )