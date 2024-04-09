package com.arttttt.profiles.impl.di

import com.arttttt.profiles.api.ProfilesComponent
import com.arttttt.profiles.impl.components.ProfilesComponentImpl
import org.koin.dsl.module

val profileFeatureModule = module {

    single<ProfilesComponent.Factory> {
        ProfilesComponent.Factory { context ->
            ProfilesComponentImpl(
                context = context,
            )
        }
    }
}