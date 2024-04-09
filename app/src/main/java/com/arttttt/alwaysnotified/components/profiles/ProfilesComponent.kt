package com.arttttt.alwaysnotified.components.profiles

import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.value.Value
import com.arttttt.alwaysnotified.domain.entity.profiles.Profile
import com.arttttt.alwaysnotified.ui.base.ListItem
import com.arttttt.core.arch.DecomposeComponent
import com.arttttt.core.arch.events.consumer.EventsConsumer
import com.arttttt.core.arch.events.producer.EventsProducer
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface ProfilesComponent : DecomposeComponent,
    EventsProducer<ProfilesComponent.Events.Output>,
    EventsConsumer<ProfilesComponent.Events.Input> {

    data class State(
        val currentProfile: Profile?,
        val isCurrentProfileDirty: Boolean,
    )

    data class UiState(
        val items: List<ListItem>,
    )

    sealed class Command {

        data class ShowMessage(
            val message: String
        ) : Command()
    }

    sealed class Events {

        sealed class Output : Events()

        sealed class Input {

            data object MarkCurrentProfileAsDirty : Input()
            data object UpdateCurrentProfile : Input()
        }
    }

    val uiState: Value<UiState>

    val states: StateFlow<State>

    val commands: Flow<Command>

    val dialog: Value<ChildSlot<*, DecomposeComponent>>

    fun profileClicked(id: String)
    fun profileActionsLongPressed(id: String)
    fun addProfileClicked()
}