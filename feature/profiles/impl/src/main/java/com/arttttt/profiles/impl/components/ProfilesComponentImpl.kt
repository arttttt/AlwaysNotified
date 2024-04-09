package com.arttttt.profiles.impl.components

import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.router.slot.SlotNavigation
import com.arkivanov.decompose.router.slot.activate
import com.arkivanov.decompose.router.slot.childSlot
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.arkivanov.mvikotlin.extensions.coroutines.states
import com.arttttt.core.arch.DecomposeComponent
import com.arttttt.core.arch.content.ComponentContent
import com.arttttt.core.arch.context.AppComponentContext
import com.arttttt.core.arch.context.wrapComponentContext
import com.arttttt.core.arch.events.producer.EventsProducerDelegate
import com.arttttt.core.arch.events.producer.EventsProducerDelegateImpl
import com.arttttt.core.arch.koinScope
import com.arttttt.profiles.api.ProfilesComponent
import com.arttttt.profiles.impl.components.di.profilesModule
import com.arttttt.profiles.impl.domain.store.ProfilesStore
import com.arttttt.profiles.impl.ui.ProfilesContent
import com.arttttt.profiles.impl.ui.lazylist.models.ProfileListItem
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import org.koin.core.component.getScopeId
import org.koin.core.qualifier.qualifier

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

    private val coroutineScope = coroutineScope()

    private val koinScope = koinScope(
        profilesModule,
        scopeID = getScopeId(),
        qualifier = qualifier<ProfilesComponent>(),
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
        .stateIn(
            coroutineScope,
            SharingStarted.WhileSubscribed(5000),
            ProfilesComponent.UiState(
                items = listOf()
            ),
        )

    override val commands: MutableSharedFlow<ProfilesComponent.Command> = MutableSharedFlow()

    override val content: ComponentContent = ProfilesContent(this)

    private val dialogNavigation = SlotNavigation<DialogConfig>()

    override val dialog: Value<ChildSlot<*, DecomposeComponent>> =
        childSlot(
            source = dialogNavigation,
            serializer = null,
            handleBackButton = true,
        ) { config, childComponentContext ->
            val wrappedContext = wrapComponentContext(
                context = childComponentContext,
                parentScopeID = koinScope.id,
            )

            TODO()

            /*when (config) {
                is DialogConfig.ProfileActions -> {
                    ProfileActionsComponentImpl(
                        context = wrappedContext,
                        profileUUID = config.id,
                    )
                }
                is DialogConfig.AddProfile -> {
                    AddProfileComponentImpl(
                        context = wrappedContext,
                    )
                }
                is DialogConfig.RemoveProfile -> {
                    RemoveProfileComponentImpl(
                        context = wrappedContext,
                        profileUUID = config.id,
                    )
                }
            }*/
        }

    init {
        /*dialog
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
                        addSelectedApps = event.addSelectedApps,
                    )
                )
            }
            .launchIn(coroutineScope)*/
    }

    override fun consume(event: ProfilesComponent.Events.Input) {
        when (event) {
            is ProfilesComponent.Events.Input.MarkCurrentProfileAsDirty -> profilesStore.accept(
                ProfilesStore.Intent.MarkCurrentProfileAsDirty)
            is ProfilesComponent.Events.Input.UpdateCurrentProfile -> profilesStore.accept(
                ProfilesStore.Intent.UpdateCurrentProfile)
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