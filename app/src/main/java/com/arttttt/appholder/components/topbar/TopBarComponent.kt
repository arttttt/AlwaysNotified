package com.arttttt.appholder.components.topbar

import com.arkivanov.decompose.value.Value
import com.arttttt.appholder.arch.shared.DecomposeComponent
import com.arttttt.appholder.arch.shared.events.consumer.EventsConsumer
import com.arttttt.appholder.arch.shared.events.producer.EventsProducer
import com.arttttt.appholder.components.topbar.actions.ExpandableTopBarAction
import com.arttttt.appholder.components.topbar.actions.TopBarAction
import com.arttttt.appholder.domain.entity.profiles.Profile
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

    val states: StateFlow<State>

    val uiState: Value<UiState>

    fun actionClicked(action: TopBarAction)
}