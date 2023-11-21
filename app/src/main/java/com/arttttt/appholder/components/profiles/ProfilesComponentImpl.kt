package com.arttttt.appholder.components.profiles

import com.arkivanov.decompose.value.operator.map
import com.arkivanov.essenty.lifecycle.doOnDestroy
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.arkivanov.mvikotlin.extensions.coroutines.states
import com.arttttt.appholder.arch.shared.DecomposeComponent
import com.arttttt.appholder.arch.shared.context.AppComponentContext
import com.arttttt.appholder.arch.shared.events.producer.EventsProducerDelegate
import com.arttttt.appholder.arch.shared.events.producer.EventsProducerDelegateImpl
import com.arttttt.appholder.components.profiles.di.profilesModule
import com.arttttt.appholder.domain.store.profiles.ProfilesStore
import com.arttttt.appholder.ui.profiles.lazylist.models.ProfileListItem
import com.arttttt.appholder.utils.extensions.asValue
import com.arttttt.appholder.utils.extensions.koinScope
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class ProfilesComponentImpl(
    context: AppComponentContext
) : ProfilesComponent,
    DecomposeComponent,
    AppComponentContext by context,
    EventsProducerDelegate<ProfilesComponent.Events.Output> by EventsProducerDelegateImpl() {

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

    init {
        lifecycle.doOnDestroy {
            coroutineScope.coroutineContext.cancelChildren()
        }
    }

    override fun consume(event: ProfilesComponent.Events.Input) {
        when (event) {
            is ProfilesComponent.Events.Input.MarkCurrentProfileAsDirty -> profilesStore.accept(ProfilesStore.Intent.MarkCurrentProfileAsDirty)
            is ProfilesComponent.Events.Input.UpdateCurrentProfile -> profilesStore.accept(ProfilesStore.Intent.UpdateCurrentProfile)
        }
    }

    override fun addProfile(title: String, color: Int) {
        profilesStore.accept(
            ProfilesStore.Intent.CreateProfile(
                title = title,
                color = color,
            )
        )
    }

    override fun removeProfile(id: String) {
        profilesStore.accept(
            ProfilesStore.Intent.RemoveProfile(
                id = id,
            )
        )
    }

    override fun profileClicked(id: String) {
        profilesStore.accept(
            ProfilesStore.Intent.SelectProfile(
                id = id,
            )
        )
    }
}