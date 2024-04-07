package com.arttttt.alwaysnotified.arch.shared.context

import com.arkivanov.decompose.GenericComponentContext
import org.koin.core.scope.ScopeID

interface AppComponentContext : GenericComponentContext<AppComponentContext> {

    val parentScopeID: ScopeID?
}