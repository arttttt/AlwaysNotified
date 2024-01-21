package com.arttttt.appholder.components.profiles

import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.router.slot.SlotNavigation
import com.arkivanov.decompose.router.slot.activate
import com.arkivanov.decompose.router.slot.dismiss
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.operator.map
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.arkivanov.mvikotlin.extensions.coroutines.states
import com.arttttt.appholder.arch.shared.DecomposeComponent
import com.arttttt.appholder.arch.shared.context.AppComponentContext
import com.arttttt.appholder.arch.shared.context.customChildSlot
import com.arttttt.appholder.arch.shared.events.producer.EventsProducer
import com.arttttt.appholder.arch.shared.events.producer.EventsProducerDelegate
import com.arttttt.appholder.arch.shared.events.producer.EventsProducerDelegateImpl
import com.arttttt.appholder.arch.shared.slotComponentEvents
import com.arttttt.appholder.arch.shared.slotDismissEvents
import com.arttttt.appholder.components.addprofile.AddProfileComponent
import com.arttttt.appholder.components.addprofile.AddProfileComponentImpl
import com.arttttt.appholder.components.profileactions.ProfileActionsComponent
import com.arttttt.appholder.components.profileactions.ProfileActionsComponentImpl
import com.arttttt.appholder.components.profiles.di.profilesModule
import com.arttttt.appholder.components.removeprofile.RemoveProfileComponent
import com.arttttt.appholder.components.removeprofile.RemoveProfileComponentImpl
import com.arttttt.appholder.domain.store.profiles.ProfilesStore
import com.arttttt.appholder.ui.profiles.lazylist.models.ProfileListItem
import com.arttttt.appholder.utils.extensions.asValue
import com.arttttt.appholder.utils.extensions.koinScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn

class ProfilesComponentImpl(
    context: AppComponentContext
) : ProfilesComponent,
    AppComponentContext by context,
    EventsProducerDelegate<ProfilesComponent.Events.Output> by EventsProducerDelegateImpl() {

    private sealed class DialogConfig {

        data object AddProfile : DialogConfig()
        data class RemoveProfile(val id: String) : DialogConfig()
        data class ProfileActions(val id: String) : DialogConfig()
    }

    private val koinScope = koinScope<ProfilesComponent>(
        profilesModule,
    )

    private val profilesStore by koinScope.inject<ProfilesStore>()

    override val states: StateFlow<ProfilesComponent.State> = profilesStore
        .states
        .map { state ->
            ProfilesComponent.State(
                currentProfile = state.selectedProfile,
                isCurrentProfileDirty = state.isCurrentProfileDirty,
            )
        }
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.Eagerly,
            initialValue = ProfilesComponent.State(
                currentProfile = null,
                isCurrentProfileDirty = false,
            ),
        )

    override val uiState = profilesStore
        .stateFlow
        .asValue()
        .map { state ->
            ProfilesComponent.UiState(
                items = state.profiles.map { profile ->
                    ProfileListItem(
                        id = profile.uuid,
                        title = profile.title,
                        color = profile.color,
                        isSelected = profile == state.selectedProfile,
                    )
                },
            )
        }

    override val commands: MutableSharedFlow<ProfilesComponent.Command> = MutableSharedFlow()

    private val dialogNavigation = SlotNavigation<DialogConfig>()

    override val dialog: Value<ChildSlot<*, DecomposeComponent>> =
        customChildSlot(
            parentScopeID = koinScope.id,
            source = dialogNavigation,
            serializer = null,
            handleBackButton = true,
        ) { config, childComponentContext ->
            when (config) {
                is DialogConfig.ProfileActions -> {
                    ProfileActionsComponentImpl(
                        context = childComponentContext,
                        profileUUID = config.id,
                    )
                }
                is DialogConfig.AddProfile -> {
                    AddProfileComponentImpl(
                        context = context,
                    )
                }
                is DialogConfig.RemoveProfile -> {
                    RemoveProfileComponentImpl(
                        context = context,
                        profileUUID = config.id,
                    )
                }
            }
        }

    init {
        dialog
            .slotDismissEvents()
            .onEach { dialogNavigation.dismiss() }
            .launchIn(coroutineScope)

        dialog
            .slotComponentEvents<EventsProducer<ProfileActionsComponent.Event>>()
            .filterIsInstance<ProfileActionsComponent.Event.RemoveProfile>()
            .onEach { event ->
                if (profilesStore.state.profiles.size > 1) {
                    dialogNavigation.activate(DialogConfig.RemoveProfile(event.uuid))
                } else {
                    commands.emit(ProfilesComponent.Command.ShowMessage("Can't remove the last profile"))
                    dialogNavigation.dismiss()
                }
            }
            .launchIn(coroutineScope)

        dialog
            .slotComponentEvents<EventsProducer<ProfileActionsComponent.Event>>()
            .filterIsInstance<ProfileActionsComponent.Event.RenameProfile>()
            .onEach {
                commands.emit(ProfilesComponent.Command.ShowMessage("Not implemented yet"))
                dialogNavigation.dismiss()
            }
            .launchIn(coroutineScope)

        dialog
            .slotComponentEvents<EventsProducer<RemoveProfileComponent.Event>>()
            .filterIsInstance<RemoveProfileComponent.Event.RemoveProfile>()
            .onEach { event ->
                profilesStore.accept(
                    ProfilesStore.Intent.RemoveProfile(
                        id = event.uuid,
                    )
                )
            }
            .launchIn(coroutineScope)

        dialog
            .slotComponentEvents<EventsProducer<AddProfileComponent.Event>>()
            .filterIsInstance<AddProfileComponent.Event.CreateProfile>()
            .onEach { event ->
                profilesStore.accept(
                    ProfilesStore.Intent.CreateProfile(
                        title = event.title,
                        color = event.color,
                    )
                )
            }
            .launchIn(coroutineScope)
    }

    override fun consume(event: ProfilesComponent.Events.Input) {
        when (event) {
            is ProfilesComponent.Events.Input.MarkCurrentProfileAsDirty -> profilesStore.accept(ProfilesStore.Intent.MarkCurrentProfileAsDirty)
            is ProfilesComponent.Events.Input.UpdateCurrentProfile -> profilesStore.accept(ProfilesStore.Intent.UpdateCurrentProfile)
        }
    }

    override fun profileClicked(id: String) {
        profilesStore.accept(
            ProfilesStore.Intent.SelectProfile(
                id = id,
            )
        )
    }

    override fun profileActionsLongPressed(id: String) {
        dialogNavigation.activate(DialogConfig.ProfileActions(id))
    }

    override fun addProfileClicked() {
        dialogNavigation.activate(DialogConfig.AddProfile)
    }
}