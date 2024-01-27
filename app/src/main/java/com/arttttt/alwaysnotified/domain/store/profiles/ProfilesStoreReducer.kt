package com.arttttt.alwaysnotified.domain.store.profiles

import com.arkivanov.mvikotlin.core.store.Reducer

object ProfilesStoreReducer : Reducer<ProfilesStore.State, ProfilesStore.Message> {

    override fun ProfilesStore.State.reduce(msg: ProfilesStore.Message): ProfilesStore.State {
        return when (msg) {
            is ProfilesStore.Message.ProfilesUpdated -> copy(
                profiles = msg.profiles,
            )
            is ProfilesStore.Message.ProfileCreated -> copy(
                profiles = profiles + msg.profile,
            )
            is ProfilesStore.Message.ProgressStarted -> copy(
                isInProgress = true,
            )
            is ProfilesStore.Message.ProgressFinished -> copy(
                isInProgress = false,
            )
            is ProfilesStore.Message.ProfileSelected -> copy(
                selectedProfile = msg.profile,
                isCurrentProfileDirty = false,
            )
            is ProfilesStore.Message.CurrentProfileBecameDirty -> copy(
                isCurrentProfileDirty = true,
            )
            is ProfilesStore.Message.CurrentProfileBecameClear -> copy(
                isCurrentProfileDirty = false,
            )
        }
    }
}