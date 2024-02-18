package com.arttttt.alwaysnotified.components.profiles.di

import com.arttttt.alwaysnotified.components.profiles.ProfilesComponent
import com.arttttt.alwaysnotified.domain.entity.profiles.SelectedActivity
import com.arttttt.alwaysnotified.domain.store.apps.AppsStore
import com.arttttt.alwaysnotified.domain.store.profiles.ProfilesStoreFactory
import org.koin.dsl.module

val profilesModule = module {
    scope<ProfilesComponent> {
        scoped {
            ProfilesStoreFactory(
                storeFactory = get(),
                selectedAppsProvider = {
                    get<AppsStore>()
                        .state
                        .selectedActivities
                        ?.values
                        ?.toList()
                        ?: emptyList()
                },
                profilesRepository = get(),
            ).create()
        }
    }
}