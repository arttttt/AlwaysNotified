package com.arttttt.topbar.api

import com.arttttt.appssearch.api.AppsSearchComponent
import com.arttttt.core.arch.DecomposeComponent
import com.arttttt.core.arch.content.ComponentContentOwner
import com.arttttt.core.arch.context.AppComponentContext

interface TopBarComponent : DecomposeComponent, ComponentContentOwner {

    fun interface Factory {

        fun create(context: AppComponentContext): TopBarComponent
    }

    val appsSearchComponent: AppsSearchComponent
}