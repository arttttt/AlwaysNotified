package com.arttttt.alwaysnotified.components.profileactions

import com.arkivanov.decompose.value.Value
import com.arttttt.core.arch.dialog.DismissEventConsumer
import com.arttttt.alwaysnotified.ui.profileactions.ProfileAction
import com.arttttt.core.arch.DecomposeComponent
import com.arttttt.core.arch.dialog.DismissEventProducer
import com.arttttt.core.arch.events.producer.EventsProducer
import kotlinx.collections.immutable.ImmutableList

interface ProfileActionsComponent : DecomposeComponent,
    EventsProducer<ProfileActionsComponent.Event>,
    DismissEventConsumer,
    DismissEventProducer {

    data class UiState(
        val items: ImmutableList<ProfileAction>
    )

    sealed class Event {

        data class RenameProfile(val uuid: String) : Event()
        data class RemoveProfile(val uuid: String) : Event()
    }

    val uiState: Value<UiState>

    fun profileActionClicked(action: ProfileAction)
}