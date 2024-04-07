package com.arttttt.alwaysnotified.arch.shared.context

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.GenericComponentContext
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

fun wrapComponentContext(
    context: GenericComponentContext<*>,
    parentScopeID: ScopeID?,
): AppComponentContext {
    return DefaultAppComponentContext(
        context = context,
        parentScopeID = parentScopeID,
    )
}