package com.arttttt.appslist.impl.di

import com.arttttt.appslist.api.AppsListComponent
import com.arttttt.appslist.impl.components.AppsListComponentImpl
import org.koin.dsl.module

val appsListFeatureModule = module {
    single {
        AppsListComponent.Factory { context ->
            AppsListComponentImpl(
                context = context,
            )
        }
    }
}