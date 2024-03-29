package com.arttttt.alwaysnotified.components.topbar

import com.arkivanov.decompose.value.Value
import com.arttttt.alwaysnotified.arch.shared.DecomposeComponent
import com.arttttt.alwaysnotified.arch.shared.events.consumer.EventsConsumer
import com.arttttt.alwaysnotified.arch.shared.events.producer.EventsProducer
import com.arttttt.alwaysnotified.components.topbar.actions.ExpandableTopBarAction
import com.arttttt.alwaysnotified.components.topbar.actions.TopBarAction
import com.arttttt.alwaysnotified.domain.entity.profiles.Profile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface TopBarComponent : DecomposeComponent,
    EventsProducer<TopBarComponent.Events.Output>,
    EventsConsumer<TopBarComponent.Events.Input> {

    data class State(
        val currentProfile: Profile?,
        val isProfileDirty: Boolean,
    )

    data class UiState(
        val expandedAction: ExpandableTopBarAction?,
        val actions: List<TopBarAction>,
    )

    sealed class Events {

        sealed class Output : Events()

        sealed class Input : Events() {

            data object MarkProfileAsDirty : Input()
            data object UpdateCurrentProfile : Input()
        }
    }

    sealed class Command {

        data class ShowMessage(val message: String) : Command()
    }

    val states: StateFlow<State>

    val uiState: Value<UiState>

    val commands: Flow<Command>

    fun actionClicked(action: TopBarAction)
}