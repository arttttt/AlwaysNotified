package com.arttttt.appholder.components.profiles.di

import com.arttttt.appholder.components.profiles.ProfilesComponent
import com.arttttt.appholder.domain.entity.profiles.SelectedActivity
import com.arttttt.appholder.domain.store.apps.AppsStore
import com.arttttt.appholder.domain.store.profiles.ProfilesStoreFactory
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
                        ?.map { (pkg, activity) ->
                            SelectedActivity(
                                pkg = pkg,
                                activity = activity,
                            )
                        }
                        ?: emptyList()
                },
                profilesRepository = get(),
            ).create()
        }
    }
}