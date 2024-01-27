package com.arttttt.alwaysnotified.components.profileactions

import com.arkivanov.decompose.value.Value
import com.arttttt.alwaysnotified.arch.shared.DecomposeComponent
import com.arttttt.alwaysnotified.arch.shared.context.AppComponentContext
import com.arttttt.alwaysnotified.arch.shared.dialog.DismissEventConsumer
import com.arttttt.alwaysnotified.arch.shared.dialog.DismissEventProducer
import com.arttttt.alwaysnotified.arch.shared.events.producer.EventsProducer
import com.arttttt.alwaysnotified.ui.profileactions.ProfileAction
import kotlinx.collections.immutable.ImmutableList

interface ProfileActionsComponent : DecomposeComponent,
    AppComponentContext,
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