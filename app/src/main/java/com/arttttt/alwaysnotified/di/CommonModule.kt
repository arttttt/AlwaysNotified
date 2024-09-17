package com.arttttt.alwaysnotified.di

import com.arttttt.alwaysnotified.AppsLauncherIntentHelperImpl
import com.arttttt.alwaysnotified.utils.resources.ResourcesProviderImpl
import com.arttttt.appslist.api.AppsLauncherIntentHelper
import com.arttttt.database.AppDatabase
import com.arttttt.localization.ResourcesProvider
import org.koin.dsl.module

val commonModule = module {
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

    factory<AppsLauncherIntentHelper> {
        AppsLauncherIntentHelperImpl()
    }
}