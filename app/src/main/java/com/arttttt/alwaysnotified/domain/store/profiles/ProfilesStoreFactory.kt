package com.arttttt.alwaysnotified.domain.store.profiles

import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arttttt.alwaysnotified.domain.entity.profiles.SelectedActivity
import com.arttttt.alwaysnotified.domain.repository.ProfilesRepository

class ProfilesStoreFactory(
    private val storeFactory: StoreFactory,
    private val selectedAppsProvider: () -> List<SelectedActivity>,
    private val profilesRepository: ProfilesRepository,
) {

    fun create(): ProfilesStore {
        return object : ProfilesStore, Store<ProfilesStore.Intent, ProfilesStore.State, ProfilesStore.Label> by storeFactory.create(
            name = ProfilesStore::class.java.name,
            initialState = ProfilesStore.State(
                profiles = emptyList(),
                isInProgress = false,
                selectedProfile = null,
                isCurrentProfileDirty = false,
            ),
            bootstrapper = SimpleBootstrapper(ProfilesStore.Action.LoadProfiles),
            executorFactory = {
                ProfilesStoreExecutor(
                    selectedAppsProvider = selectedAppsProvider,
                    profilesRepository = profilesRepository,
                )
            },
            reducer = ProfilesStoreReducer,
        ) {}
    }
}