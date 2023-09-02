package com.arttttt.appholder.arch.context

import com.arkivanov.decompose.ComponentContext
import org.koin.core.scope.ScopeID

class DefaultAppComponentContext(
    context: ComponentContext,
    override val parentScopeID: ScopeID?
) : AppComponentContext, ComponentContext by context