package com.arttttt.appslist.impl.components.appslist.di

import com.arttttt.appslist.api.AppsListComponent
import com.arttttt.appslist.impl.components.app.AppComponent
import com.arttttt.appslist.impl.components.app.AppComponentImpl
import com.arttttt.appslist.impl.components.appslist.AppsListTransformer
import com.arttttt.appslist.impl.data.AppsLauncherImpl
import com.arttttt.appslist.impl.data.AppsRepositoryImpl
import com.arttttt.appslist.impl.domain.AppsLauncher
import com.arttttt.appslist.impl.domain.repository.AppsRepository
import com.arttttt.appslist.impl.domain.store.AppsStore
import com.arttttt.appslist.impl.domain.store.AppsStoreFactory
import com.arttttt.profiles.api.SelectedActivitiesRepository
import org.koin.dsl.module

internal val appsListModule = module {
    scope<AppsListComponent> {
        scoped {
            AppsListTransformer(
                resourcesProvider = get(),
            )
        }

        scoped<AppsStore> {
            AppsStoreFactory(
                storeFactory = get(),
                appsRepository = get(),
            ).create()
        }

        scoped<AppsRepository> {
            AppsRepositoryImpl(
                context = get(),
                profilesDao = get(),
            )
        }

        scoped<AppsLauncher> {
            AppsLauncherImpl(
                context = get(),
                appsStore = get(),
                intentHelper = get(),
            )
        }

        scoped {
            val appsStore = get<AppsStore>()

            SelectedActivitiesRepository {
                appsStore
                    .state
                    .selectedActivities
                    ?.values
                    ?.toList()
                    ?: emptyList()
            }
        }

        scoped {
            AppComponent.Factory(::AppComponentImpl)
        }
    }
}