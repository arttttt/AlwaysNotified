package com.arttttt.appholder.components.removeprofile

import com.arkivanov.decompose.ComponentContext
import com.arttttt.appholder.arch.shared.DecomposeComponent
import com.arttttt.appholder.arch.shared.dialog.DismissEventConsumer
import com.arttttt.appholder.arch.shared.dialog.DismissEventProducer
import com.arttttt.appholder.arch.shared.events.producer.EventsProducer

interface RemoveProfileComponent : DecomposeComponent,
    ComponentContext,
    EventsProducer<RemoveProfileComponent.Event>,
    DismissEventProducer,
    DismissEventConsumer {

    sealed class Event {

        data class RemoveProfile(val uuid: String) : Event()
    }

    fun confirmedClicked()
}