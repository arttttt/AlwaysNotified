package com.arttttt.alwaysnotified.domain.store.profiles

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.arttttt.alwaysnotified.domain.entity.profiles.Profile
import com.arttttt.alwaysnotified.domain.entity.profiles.SelectedActivity
import com.arttttt.alwaysnotified.domain.repository.ProfilesRepository
import com.arttttt.alwaysnotified.utils.extensions.createDefault
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
            is ProfilesStore.Intent.CreateProfile -> createProfile(intent.title, intent.color, intent.addSelectedApps)
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
        addSeletedApps: Boolean,
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
                    selectedActivities = if (addSeletedApps) {
                        selectedAppsProvider.invoke()
                    } else {
                        emptyList()
                    },
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
            val profiles = withContext(Dispatchers.IO) {
                profilesRepository
                    .getProfiles()
                    .takeIf { it.isNotEmpty() }
                    ?: let {
                        val profile = Profile.createDefault()
                        profilesRepository.saveProfile(
                            profile = profile,
                            selectedActivities = emptyList(),
                        )
                        listOf(profile)
                    }
            }

            dispatch(
                ProfilesStore.Message.ProfilesUpdated(
                    profiles = profiles,
                )
            )
            dispatch(
                ProfilesStore.Message.ProfileSelected(
                    profile = profiles.first(),
                )
            )
        }
    }

    private fun removeProfile(id: String) {
        if (state().profiles.size == 1) {
            publish(ProfilesStore.Label.CantRemoveProfile)
        } else {
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

                state()
                    .profiles
                    .first()
                    .let(ProfilesStore.Message::ProfileSelected)
                    .let(::dispatch)
            }
        }
    }
}