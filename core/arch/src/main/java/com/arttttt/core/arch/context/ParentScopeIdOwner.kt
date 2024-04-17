package com.arttttt.core.arch.context

import org.koin.core.scope.ScopeID

interface ParentScopeIdOwner {

    val parentScopeID: ScopeID?
}