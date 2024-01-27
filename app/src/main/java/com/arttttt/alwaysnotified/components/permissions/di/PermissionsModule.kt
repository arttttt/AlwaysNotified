package com.arttttt.alwaysnotified.components.permissions.di

import com.arttttt.alwaysnotified.data.repository.PermissionsRepositoryImpl
import com.arttttt.alwaysnotified.domain.repository.PermissionsRepository
import com.arttttt.alwaysnotified.domain.store.permissions.PermissionsStore
import com.arttttt.alwaysnotified.domain.store.permissions.PermissionsStoreFactory
import com.arttttt.alwaysnotified.components.permissions.PermissionsComponent
import org.koin.dsl.module

val permissionsModule = module {

    scope<PermissionsComponent> {

        scoped<PermissionsRepository> {
            PermissionsRepositoryImpl(
                context = get(),
            )
        }

        scoped<PermissionsStore> {
            PermissionsStoreFactory(
                storeFactory = get(),
                repository = get(),
                permissionsRequester = get(),
            ).create()
        }
    }
}