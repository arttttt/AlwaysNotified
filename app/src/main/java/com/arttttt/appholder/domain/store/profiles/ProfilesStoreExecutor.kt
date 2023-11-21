package com.arttttt.appholder.domain.store.profiles

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.arttttt.appholder.domain.entity.profiles.Profile
import com.arttttt.appholder.domain.entity.profiles.SelectedActivity
import com.arttttt.appholder.domain.repository.ProfilesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID

class ProfilesStoreExecutor(
    private val selectedAppsProvider: () -> List<SelectedActivity>,
    private val profilesRepository: ProfilesRepository,
) : CoroutineExecutor<ProfilesStore.Intent, ProfilesStore.Action, ProfilesStore.State, ProfilesStore.Message, ProfilesStore.Label>() {

    override fun executeAction(action: ProfilesStore.Action) {
        when (action) {
            is ProfilesStore.Action.LoadProfiles -> loadProfiles()
        }
    }

    override fun executeIntent(intent: ProfilesStore.Intent) {
        when (intent) {
            is ProfilesStore.Intent.CreateProfile -> createProfile(intent.title, intent.color)
            is ProfilesStore.Intent.RemoveProfile -> removeProfile(intent.id)
            is ProfilesStore.Intent.SelectProfile -> selectProfile(intent.id)
            is ProfilesStore.Intent.UpdateCurrentProfile -> updateCurrentProfile()
            is ProfilesStore.Intent.MarkCurrentProfileAsDirty -> markCurrentProfileAsDirty()
        }
    }

    private fun markCurrentProfileAsDirty() {
        dispatch(ProfilesStore.Message.CurrentProfileBecameDirty)
    }

    private fun updateCurrentProfile() {
        val profile = state().selectedProfile ?: return

        scope.launch {
            withContext(Dispatchers.IO) {
                profilesRepository.saveProfile(
                    profile = profile,
                    selectedActivities = selectedAppsProvider.invoke(),
                )
            }

            dispatch(ProfilesStore.Message.CurrentProfileBecameClear)
        }
    }

    private fun selectProfile(
        id: String,
    ) {
        state()
            .profiles
            .first { profile -> profile.uuid == id }
            .let(ProfilesStore.Message::ProfileSelected)
            .let(::dispatch)
    }

    private fun createProfile(
        title: String,
        color: Int,
    ) {
        scope.launch {
            val profile = Profile(
                uuid = UUID.nameUUIDFromBytes(title.toByteArray()).toString(),
                title = title,
                color = color,
            )

            withContext(Dispatchers.IO) {
                profilesRepository.saveProfile(
                    profile = profile,
                    selectedActivities = selectedAppsProvider.invoke(),
                )
            }

            dispatch(
                message = ProfilesStore.Message.ProfileCreated(
                    profile = profile
                )
            )

            selectProfile(
                id = state().profiles.last().uuid,
            )
        }
    }

    private fun loadProfiles() {
        scope.launch {
            dispatch(
                ProfilesStore.Message.ProfilesUpdated(
                    profiles = profilesRepository.getProfiles()
                )
            )
        }
    }

    private fun removeProfile(id: String) {
        scope.launch {
            withContext(Dispatchers.IO) {
                profilesRepository.removeProfileByUUID(id)
            }

            state()
                .profiles
                .toMutableList()
                .filter { profile -> profile.uuid != id }
                .toList()
                .let(ProfilesStore.Message::ProfilesUpdated)
                .let(::dispatch)
        }
    }
}