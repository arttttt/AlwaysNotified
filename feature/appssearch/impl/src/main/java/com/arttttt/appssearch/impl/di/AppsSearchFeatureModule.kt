package com.arttttt.appssearch.impl.di

import com.arttttt.appssearch.api.AppsSearchComponent
import com.arttttt.appssearch.impl.components.AppsSearchComponentImpl
import org.koin.dsl.module

val appsSearchFeatureModule = module {
    single {
        AppsSearchComponent.Factory { context ->
            AppsSearchComponentImpl(
                context = context,
            )
        }
    }
}