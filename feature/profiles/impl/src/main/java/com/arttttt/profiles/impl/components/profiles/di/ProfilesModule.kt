package com.arttttt.profiles.impl.components.profiles.di

import com.arttttt.profiles.api.ProfilesComponent
import com.arttttt.profiles.impl.components.addprofile.AddProfileComponent
import com.arttttt.profiles.impl.components.addprofile.AddProfileComponentImpl
import com.arttttt.profiles.impl.components.profileactions.ProfileActionsComponent
import com.arttttt.profiles.impl.components.profileactions.ProfileActionsComponentImpl
import com.arttttt.profiles.impl.components.removeprofile.RemoveProfileComponent
import com.arttttt.profiles.impl.components.removeprofile.RemoveProfileComponentImpl
import com.arttttt.profiles.impl.domain.store.ProfilesStoreFactory
import org.koin.dsl.module

internal val profilesModule = module {
    scope<ProfilesComponent> {
        scoped {
            AddProfileComponent.Factory { context ->
                AddProfileComponentImpl(
                    context = context,
                )
            }
        }

        scoped {
            RemoveProfileComponent.Factory { context, profileUUID ->
                RemoveProfileComponentImpl(
                    context = context,
                    profileUUID = profileUUID,
                )
            }
        }

        scoped {
            ProfileActionsComponent.Factory { context, profilesUUID ->
                ProfileActionsComponentImpl(
                    profileUUID = profilesUUID,
                    context = context,
                )
            }
        }

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