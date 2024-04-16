package com.arttttt.alwaysnotified.di

import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.logging.store.LoggingStoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.arttttt.alwaysnotified.AppsLauncher
import com.arttttt.alwaysnotified.AppsLauncherImpl
import com.arttttt.alwaysnotified.utils.resources.ResourcesProviderImpl
import com.arttttt.appslist.api.AppsManager
import com.arttttt.database.AppDatabase
import com.arttttt.localization.ResourcesProvider
import com.arttttt.profiles.api.SelectedActivitiesRepository
import org.koin.dsl.module

val commonModule = module {
    single<StoreFactory> {
        LoggingStoreFactory(
            delegate = DefaultStoreFactory()
        )
    }

    single<AppsLauncher> {
        AppsLauncherImpl(
            context = get(),
            appsManager = get(),
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

    single<ResourcesProvider> {
        ResourcesProviderImpl(
            context = get(),
        )
    }

    factory {
        SelectedActivitiesRepository {
            get<AppsManager>()
                .selectedActivities
                ?.values
                ?.toList()
                ?: emptyList()
        }
    }
}