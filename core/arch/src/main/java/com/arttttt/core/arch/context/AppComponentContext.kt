package com.arttttt.core.arch.context

import com.arkivanov.decompose.GenericComponentContext

interface AppComponentContext : GenericComponentContext<AppComponentContext>,
    ParentScopeIdOwner