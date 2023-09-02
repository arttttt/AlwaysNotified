package com.arttttt.appholder.di

import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.arttttt.appholder.AppsLauncher
import com.arttttt.appholder.data.AppsRepositoryImpl
import com.arttttt.appholder.domain.repository.AppsRepository
import com.arttttt.appholder.domain.store.AppsStore
import com.arttttt.appholder.domain.store.AppsStoreFactory
import org.koin.dsl.module

val commonModule = module {
    single<AppsRepository> {
        AppsRepositoryImpl(
            context = get(),
        )
    }

    single<AppsStore> {
        AppsStoreFactory(
            storeFactory = DefaultStoreFactory(),
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