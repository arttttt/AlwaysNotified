package com.arttttt.appholder.domain.store.profiles

import com.arkivanov.mvikotlin.core.store.Store
import com.arttttt.appholder.domain.entity.profiles.Profile

interface ProfilesStore : Store<ProfilesStore.Intent, ProfilesStore.State, ProfilesStore.Label> {

    data class State(
        val selectedProfile: Profile?,
        val isCurrentProfileDirty: Boolean,
        val isInProgress: Boolean,
        val profiles: List<Profile>,
    )

    sealed class Action {
        data object LoadProfiles : Action()
    }

    sealed class Intent {
        data class CreateProfile(
            val title: String,
            val color: Int,
            val addSelectedApps: Boolean,
        ) : Intent()

        data class RemoveProfile(
            val id: String,
        ) : Intent()

        data class SelectProfile(
            val id: String,
        ) : Intent()

        data object UpdateCurrentProfile : Intent()
        data object MarkCurrentProfileAsDirty : Intent()
    }

    sealed class Message {
        data class ProfilesUpdated(
            val profiles: List<Profile>,
        ) : Message()

        data class ProfileCreated(
            val profile: Profile,
        ) : Message()

        data object ProgressStarted : Message()
        data object ProgressFinished : Message()

        data class ProfileSelected(
            val profile: Profile,
        ) : Message()

        data object CurrentProfileBecameDirty : Message()
        data object CurrentProfileBecameClear : Message()
    }

    sealed class Label {

        data object CantRemoveProfile : Label()
    }
}