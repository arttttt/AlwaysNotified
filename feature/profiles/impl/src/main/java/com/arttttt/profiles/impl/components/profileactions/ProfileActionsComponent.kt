package com.arttttt.profiles.impl.components.profileactions

import com.arkivanov.decompose.value.Value
import com.arttttt.core.arch.dialog.DismissEventConsumer
import com.arttttt.core.arch.DecomposeComponent
import com.arttttt.core.arch.context.AppComponentContext
import com.arttttt.core.arch.dialog.DismissEventProducer
import com.arttttt.core.arch.events.producer.EventsProducer
import com.arttttt.profiles.impl.ui.profileactions.ProfileAction
import kotlinx.collections.immutable.ImmutableList

interface ProfileActionsComponent : DecomposeComponent,
    EventsProducer<ProfileActionsComponent.Event>,
    DismissEventConsumer,
    DismissEventProducer {

    fun interface Factory {

        fun create(
            context: AppComponentContext,
            profileUUID: String,
        ): ProfileActionsComponent
    }

    data class UiState(
        val items: ImmutableList<ProfileAction>,
    )

    sealed class Event {

        data class RenameProfile(val uuid: String) : Event()
        data class RemoveProfile(val uuid: String) : Event()
    }

    val uiState: Value<UiState>

    fun profileActionClicked(action: ProfileAction)
}