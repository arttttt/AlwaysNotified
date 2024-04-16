package com.arttttt.appslist.impl.di

import com.arttttt.appslist.impl.domain.repository.AppsRepository
import com.arttttt.appslist.api.AppsListComponent
import com.arttttt.appslist.api.AppsManager
import com.arttttt.appslist.impl.components.AppsListComponentImpl
import com.arttttt.appslist.impl.data.AppsRepositoryImpl
import com.arttttt.appslist.impl.domain.AppsManagerImpl
import com.arttttt.appslist.impl.domain.store.AppsStore
import com.arttttt.appslist.impl.domain.store.AppsStoreFactory
import org.koin.dsl.module

val appsListFeatureModule = module {
    single {
        AppsListComponent.Factory { context ->
            AppsListComponentImpl(
                context = context,
            )
        }
    }

    single<AppsRepository> {
        AppsRepositoryImpl(
            context = get(),
            profilesDao = get(),
        )
    }

    single<AppsStore> {
        AppsStoreFactory(
            storeFactory = get(),
            appsRepository = get(),
        ).create()
    }

    single<AppsManager> {
        AppsManagerImpl(
            appsStore = get()
        )
    }
}