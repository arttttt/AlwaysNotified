package com.arttttt.topbar.impl.di

import com.arttttt.topbar.api.TopBarComponent
import com.arttttt.topbar.impl.components.TopBarComponentImpl
import org.koin.dsl.module

val topBarFeatureModule = module {
    single {
        TopBarComponent.Factory { context ->
            TopBarComponentImpl(
                context = context,
            )
        }
    }
}