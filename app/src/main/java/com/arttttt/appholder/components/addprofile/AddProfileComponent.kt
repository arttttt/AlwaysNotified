package com.arttttt.appholder.components.addprofile

import com.arttttt.appholder.arch.shared.DecomposeComponent
import com.arttttt.appholder.arch.shared.context.AppComponentContext
import com.arttttt.appholder.arch.shared.dialog.DismissEventConsumer
import com.arttttt.appholder.arch.shared.dialog.DismissEventProducer

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
}