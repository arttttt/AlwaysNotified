package com.arttttt.appholder.di

import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.logging.store.LoggingStoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.arttttt.appholder.AppsLauncher
import com.arttttt.appholder.data.database.AppDatabase
import com.arttttt.appholder.data.repository.AppsRepositoryImpl
import com.arttttt.appholder.data.repository.ProfilesRepositoryImpl
import com.arttttt.appholder.domain.repository.AppsRepository
import com.arttttt.appholder.domain.repository.ProfilesRepository
import com.arttttt.appholder.domain.store.apps.AppsStore
import com.arttttt.appholder.domain.store.apps.AppsStoreFactory
import com.arttttt.appholder.utils.resources.ResourcesProvider
import com.arttttt.appholder.utils.resources.ResourcesProviderImpl
import org.koin.dsl.module

val commonModule = module {
    single<StoreFactory> {
        LoggingStoreFactory(
            delegate = DefaultStoreFactory()
        )
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

    single {
        AppsLauncher(
            context = get(),
            appsStore = get(),
        )
    }

    single {
        AppDatabase.create(
            context = get(),
        )
    }

    single {
        get<AppDatabase>().profilesDao()
    }

    single<ProfilesRepository> {
        ProfilesRepositoryImpl(
            profilesDao = get(),
        )
    }

    single<ResourcesProvider> {
        ResourcesProviderImpl(
            context = get(),
        )
    }
}