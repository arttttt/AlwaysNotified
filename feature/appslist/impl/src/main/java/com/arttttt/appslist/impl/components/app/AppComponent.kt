package com.arttttt.appslist.impl.components.app

import com.arttttt.core.arch.DecomposeComponent
import com.arttttt.core.arch.content.ComponentContentOwner
import com.arttttt.core.arch.context.AppComponentContext
import com.arttttt.core.arch.dialog.DismissEventConsumer
import com.arttttt.core.arch.dialog.DismissEventProducer

internal interface AppComponent : DecomposeComponent,
    ComponentContentOwner,
    DismissEventConsumer,
    DismissEventProducer {

    fun interface Factory {

        fun create(context: AppComponentContext): AppComponent
    }
}