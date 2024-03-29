package com.arttttt.alwaysnotified.components.profiles

import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.value.Value
import com.arttttt.alwaysnotified.arch.shared.DecomposeComponent
import com.arttttt.alwaysnotified.arch.shared.context.AppComponentContext
import com.arttttt.alwaysnotified.arch.shared.events.consumer.EventsConsumer
import com.arttttt.alwaysnotified.arch.shared.events.producer.EventsProducer
import com.arttttt.alwaysnotified.domain.entity.profiles.Profile
import com.arttttt.alwaysnotified.ui.base.ListItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface ProfilesComponent : DecomposeComponent,
    AppComponentContext,
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