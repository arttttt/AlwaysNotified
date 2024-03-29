package com.arttttt.alwaysnotified.components.removeprofile

import com.arkivanov.decompose.ComponentContext
import com.arttttt.alwaysnotified.arch.shared.DecomposeComponent
import com.arttttt.alwaysnotified.arch.shared.dialog.DismissEventConsumer
import com.arttttt.alwaysnotified.arch.shared.dialog.DismissEventProducer
import com.arttttt.alwaysnotified.arch.shared.events.producer.EventsProducer

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