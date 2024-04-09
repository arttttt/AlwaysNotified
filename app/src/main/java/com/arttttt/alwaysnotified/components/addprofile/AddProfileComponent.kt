package com.arttttt.alwaysnotified.components.addprofile

import com.arttttt.core.arch.DecomposeComponent
import com.arttttt.core.arch.dialog.DismissEventConsumer
import com.arttttt.core.arch.dialog.DismissEventProducer

interface AddProfileComponent : DecomposeComponent,
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