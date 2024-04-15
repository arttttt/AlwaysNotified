package com.arttttt.permissions.impl.components.di

import com.arttttt.permissions.api.PermissionsComponent
import com.arttttt.permissions.impl.data.repository.PermissionsRepositoryImpl
import com.arttttt.permissions.impl.domain.repository.PermissionsRepository
import com.arttttt.permissions.impl.domain.store.PermissionsStore
import com.arttttt.permissions.impl.domain.store.PermissionsStoreFactory
import org.koin.dsl.module

internal val permissionsModule = module {

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