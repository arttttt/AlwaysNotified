package com.arttttt.alwaysnotified.di

import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.logging.store.LoggingStoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.arttttt.alwaysnotified.AppsLauncher
import com.arttttt.alwaysnotified.AppsLauncherImpl
import com.arttttt.alwaysnotified.data.database.AppDatabase
import com.arttttt.alwaysnotified.data.repository.AppsRepositoryImpl
import com.arttttt.alwaysnotified.data.repository.ProfilesRepositoryImpl
import com.arttttt.alwaysnotified.AppsRepository
import com.arttttt.profiles.api.ProfilesRepository
import com.arttttt.appslist.impl.domain.store.AppsStore
import com.arttttt.appslist.impl.domain.store.AppsStoreFactory
import com.arttttt.localization.ResourcesProvider
import com.arttttt.alwaysnotified.utils.resources.ResourcesProviderImpl
import com.arttttt.profiles.api.SelectedActivitiesRepository
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

    single<AppsLauncher> {
        AppsLauncherImpl(
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

    factory {
        SelectedActivitiesRepository {
            get<AppsStore>()
                .state
                .selectedActivities
                ?.values
                ?.toList()
                ?: emptyList()
        }
    }
}