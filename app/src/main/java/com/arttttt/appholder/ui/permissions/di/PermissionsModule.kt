package com.arttttt.appholder.ui.permissions.di

import com.arttttt.appholder.data.PermissionsRepositoryImpl
import com.arttttt.appholder.domain.repository.PermissionsRepository
import com.arttttt.appholder.domain.store.permissions.PermissionsStore
import com.arttttt.appholder.domain.store.permissions.PermissionsStoreFactory
import com.arttttt.appholder.ui.permissions.PermissionsComponent
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