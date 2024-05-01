package com.arttttt.appssearch.api

import com.arttttt.core.arch.DecomposeComponent
import com.arttttt.core.arch.content.ComponentContentOwner
import com.arttttt.core.arch.context.AppComponentContext
import kotlinx.coroutines.flow.StateFlow

interface AppsSearchComponent : DecomposeComponent, ComponentContentOwner {

    fun interface Factory {

        fun create(context: AppComponentContext): AppsSearchComponent
    }

    data class State(
        val filter: String
    )

    val states: StateFlow<State>
}