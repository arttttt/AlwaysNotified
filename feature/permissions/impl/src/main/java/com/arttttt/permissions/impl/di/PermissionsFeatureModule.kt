package com.arttttt.permissions.impl.di

import com.arttttt.permissions.api.PermissionsComponent
import com.arttttt.permissions.impl.components.PermissionsComponentImpl
import org.koin.dsl.module

val permissionsFeatureModule = module {
    single {
        PermissionsComponent.Factory { context ->
            PermissionsComponentImpl(
                context = context,
            )
        }
    }
}