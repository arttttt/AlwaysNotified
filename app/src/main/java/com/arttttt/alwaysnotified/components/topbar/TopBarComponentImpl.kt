package com.arttttt.alwaysnotified.components.topbar

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.update
import com.arkivanov.essenty.lifecycle.doOnDestroy
import com.arttttt.alwaysnotified.arch.shared.context.AppComponentContext
import com.arttttt.alwaysnotified.arch.shared.context.childAppContext
import com.arttttt.alwaysnotified.arch.shared.events.producer.EventsProducerDelegate
import com.arttttt.alwaysnotified.arch.shared.events.producer.EventsProducerDelegateImpl
import com.arttttt.alwaysnotified.components.profiles.ProfilesComponentImpl
import com.arttttt.alwaysnotified.components.profiles.ProfilesComponent
import com.arttttt.alwaysnotified.components.topbar.actions.ExpandableTopBarAction
import com.arttttt.alwaysnotified.components.topbar.actions.TopBarAction
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@Suppress("IntroduceWhenSubject")
class TopBarComponentImpl(
    componentContext: AppComponentContext,
) : TopBarComponent,
    AppComponentContext by componentContext,
    EventsProducerDelegate<TopBarComponent.Events.Output> by EventsProducerDelegateImpl() {

    private val profilesComponent = ProfilesComponentImpl(
        context = childAppContext("profiles")
    )

    override val states: StateFlow<TopBarComponent.State> = profilesComponent
        .states
        .map { state ->
            TopBarComponent.State(
                currentProfile = state.currentProfile,
                isProfileDirty = state.isCurrentProfileDirty,
            )
        }
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.Eagerly,
            initialValue = TopBarComponent.State(
                currentProfile = null,
                isProfileDirty = false,
            ),
        )

    override val uiState = MutableValue(
        initialValue = TopBarComponent.UiState(
            expandedAction = null,
            actions = listOf(
                TopBarAction.Profiles(
                    component = profilesComponent
                ),
                TopBarAction.Settings,
            ),
        )
    )

    override val commands = MutableSharedFlow<TopBarComponent.Command>(extraBufferCapacity = 1)

    init {
        lifecycle.doOnDestroy { coroutineScope.coroutineContext.cancelChildren() }
    }

    override fun consume(event: TopBarComponent.Events.Input) {
        when (event) {
            is TopBarComponent.Events.Input.MarkProfileAsDirty -> profilesComponent.consume(ProfilesComponent.Events.Input.MarkCurrentProfileAsDirty)
            is TopBarComponent.Events.Input.UpdateCurrentProfile -> profilesComponent.consume(ProfilesComponent.Events.Input.UpdateCurrentProfile)
        }
    }

    override fun actionClicked(action: TopBarAction) {
        when {
            action is ExpandableTopBarAction -> toggleExpand(action)
            action is TopBarAction.Settings -> {
                commands.tryEmit(TopBarComponent.Command.ShowMessage("Not implemented yet"))
            }
            else -> { /** TODO */ }
        }
    }

    private fun toggleExpand(action: ExpandableTopBarAction) {
        uiState.update { state ->
            state.copy(
                expandedAction = action.takeIf { action != state.expandedAction }
            )
        }
    }
}