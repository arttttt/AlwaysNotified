package com.arttttt.appholder.arch.shared.context

import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.CoroutineScope
import org.koin.core.scope.ScopeID

class DefaultAppComponentContext(
    context: ComponentContext,
    override val parentScopeID: ScopeID?,
    override val coroutineScope: CoroutineScope,
) : AppComponentContext, ComponentContext by context