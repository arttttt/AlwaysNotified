package com.arttttt.appholder.arch.shared.context

import android.util.Log
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.childContext
import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.router.slot.SlotNavigationSource
import com.arkivanov.decompose.router.slot.childSlot
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigationSource
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.Lifecycle
import com.arkivanov.essenty.lifecycle.doOnDestroy
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import kotlinx.serialization.KSerializer
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
        coroutineScope.coroutineContext.cancelChildren()
    }

    return DefaultAppComponentContext(
        context = context,
        parentScopeID = parentScopeID,
        coroutineScope = coroutineScope,
    )
}

inline fun <reified C : Any, T : Any> AppComponentContext.customChildStack(
    parentScopeID: ScopeID?,
    serializer: KSerializer<C>,
    source: StackNavigationSource<C>,
    initialConfiguration: C,
    key: String = "DefaultChildStack",
    handleBackButton: Boolean = false,
    noinline childFactory: (configuration: C, AppComponentContext) -> T
): Value<ChildStack<C, T>> =
    childStack(
        source = source,
        serializer = serializer,
        initialStack = { listOf(initialConfiguration) },
        key = key,
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

inline fun <reified C : Any, T : Any> AppComponentContext.customChildSlot(
    parentScopeID: ScopeID?,
    serializer: KSerializer<C>?,
    source: SlotNavigationSource<C>,
    noinline initialConfiguration: () -> C? = { null },
    key: String = "DefaultChildSlot",
    handleBackButton: Boolean,
    noinline childFactory: (configuration: C, AppComponentContext) -> T
): Value<ChildSlot<C, T>> =
    childSlot(
        source = source,
        serializer = serializer,
        handleBackButton = handleBackButton,
        initialConfiguration = initialConfiguration,
        key = key,
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
        }
    )