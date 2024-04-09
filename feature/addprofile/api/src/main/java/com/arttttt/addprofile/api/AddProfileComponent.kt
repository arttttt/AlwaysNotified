package com.arttttt.addprofile.api

import com.arttttt.core.arch.DecomposeComponent
import com.arttttt.core.arch.content.ComponentContentOwner
import com.arttttt.core.arch.context.AppComponentContext
import com.arttttt.core.arch.dialog.DismissEventConsumer
import com.arttttt.core.arch.dialog.DismissEventProducer

interface AddProfileComponent : DecomposeComponent,
    ComponentContentOwner,
    DismissEventConsumer,
    DismissEventProducer {

    fun interface Factory {

        fun create(context: AppComponentContext): AddProfileComponent
    }

    sealed class Event {

        data class CreateProfile(
            val title: String,
            val color: Int,
            val addSelectedApps: Boolean,
        ) : Event()
    }

    fun createProfileClicked(
        title: String,
        color: Int,
        addSelectedApps: Boolean,
    )

    suspend fun canCreateProfile(title: String): Boolean
}