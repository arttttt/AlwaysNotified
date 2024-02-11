package com.arttttt.alwaysnotified.components.addprofile

import com.arttttt.alwaysnotified.arch.shared.DecomposeComponent
import com.arttttt.alwaysnotified.arch.shared.context.AppComponentContext
import com.arttttt.alwaysnotified.arch.shared.dialog.DismissEventConsumer
import com.arttttt.alwaysnotified.arch.shared.dialog.DismissEventProducer

interface AddProfileComponent : DecomposeComponent,
    AppComponentContext,
    DismissEventConsumer,
    DismissEventProducer {

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