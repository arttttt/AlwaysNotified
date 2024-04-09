package com.arttttt.profiles.impl.components.di

import com.arttttt.profiles.api.ProfilesComponent
import com.arttttt.profiles.impl.domain.store.ProfilesStoreFactory
import org.koin.dsl.module

val profilesModule = module {
    scope<ProfilesComponent> {
        scoped {
            ProfilesStoreFactory(
                storeFactory = get(),
                selectedAppsProvider = {
                    emptyList()
                    /*get<AppsStore>()
                        .state
                        .selectedActivities
                        ?.values
                        ?.toList()
                        ?: emptyList()*/
                },
                profilesRepository = get(),
            ).create()
        }
    }
}