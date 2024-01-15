package com.arttttt.appholder.components.profileactions

import com.arkivanov.decompose.value.Value
import com.arttttt.appholder.arch.shared.DecomposeComponent
import com.arttttt.appholder.arch.shared.context.AppComponentContext
import com.arttttt.appholder.arch.shared.dialog.DismissEventConsumer
import com.arttttt.appholder.arch.shared.dialog.DismissEventProducer
import com.arttttt.appholder.arch.shared.events.producer.EventsProducer
import com.arttttt.appholder.ui.profileactions.ProfileAction
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