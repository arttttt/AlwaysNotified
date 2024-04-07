package com.arttttt.alwaysnotified.arch.shared.context

import org.koin.core.scope.ScopeID

interface ParentScopeIdOwner {

    val parentScopeID: ScopeID?
}