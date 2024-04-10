package com.arttttt.profiles.impl.domain.store

import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arttttt.profiles.api.ProfilesRepository
import com.arttttt.profiles.api.SelectedActivitiesRepository

internal class ProfilesStoreFactory(
    private val storeFactory: StoreFactory,
    private val selectedActivitiesRepository: SelectedActivitiesRepository,
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
                    selectedActivitiesRepository = selectedActivitiesRepository,
                    profilesRepository = profilesRepository,
                )
            },
            reducer = ProfilesStoreReducer,
        ) {}
    }
}