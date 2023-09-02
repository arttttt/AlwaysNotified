package com.arttttt.appholder.arch.context

import com.arkivanov.decompose.ComponentContext
import org.koin.core.scope.ScopeID

interface AppComponentContext : ComponentContext {

    val parentScopeID: ScopeID?
}