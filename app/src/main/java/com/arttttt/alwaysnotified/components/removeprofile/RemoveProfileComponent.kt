package com.arttttt.alwaysnotified.components.removeprofile

import com.arttttt.core.arch.DecomposeComponent
import com.arttttt.core.arch.dialog.DismissEventConsumer
import com.arttttt.core.arch.dialog.DismissEventProducer
import com.arttttt.core.arch.events.producer.EventsProducer

interface RemoveProfileComponent : DecomposeComponent,
    EventsProducer<RemoveProfileComponent.Event>,
    DismissEventProducer,
    DismissEventConsumer {

    sealed class Event {

        data class RemoveProfile(val uuid: String) : Event()
    }

    fun confirmedClicked()
}