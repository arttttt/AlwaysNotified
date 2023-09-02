package com.arttttt.appholder.di

import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.logging.store.LoggingStoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.arttttt.appholder.AppsLauncher
import com.arttttt.appholder.data.AppsRepositoryImpl
import com.arttttt.appholder.domain.repository.AppsRepository
import com.arttttt.appholder.domain.store.apps.AppsStore
import com.arttttt.appholder.domain.store.apps.AppsStoreFactory
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
}