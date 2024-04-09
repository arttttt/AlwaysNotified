package com.arttttt.addprofile.impl.di

import com.arttttt.addprofile.api.AddProfileComponent
import com.arttttt.addprofile.impl.components.AddProfileComponentImpl
import org.koin.dsl.module

val addProfileFeatureModule = module {
    single<AddProfileComponent.Factory> {
        AddProfileComponent.Factory { context ->
            AddProfileComponentImpl(
                context = context,
            )
        }
    }
}