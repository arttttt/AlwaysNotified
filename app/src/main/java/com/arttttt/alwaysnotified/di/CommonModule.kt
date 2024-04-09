package com.arttttt.alwaysnotified.di

import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.logging.store.LoggingStoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.arttttt.addprofile.api.ProfileExistsChecker
import com.arttttt.alwaysnotified.AppsLauncher
import com.arttttt.alwaysnotified.data.database.AppDatabase
import com.arttttt.alwaysnotified.data.repository.AppsRepositoryImpl
import com.arttttt.alwaysnotified.data.repository.ProfilesRepositoryImpl
import com.arttttt.alwaysnotified.domain.repository.AppsRepository
import com.arttttt.profiles.api.ProfilesRepository
import com.arttttt.alwaysnotified.domain.store.apps.AppsStore
import com.arttttt.alwaysnotified.domain.store.apps.AppsStoreFactory
import com.arttttt.alwaysnotified.utils.resources.ResourcesProvider
import com.arttttt.alwaysnotified.utils.resources.ResourcesProviderImpl
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

    factory {
        ProfileExistsChecker { title ->
            get<ProfilesRepository>().isProfileExist(title)
        }
    }
}