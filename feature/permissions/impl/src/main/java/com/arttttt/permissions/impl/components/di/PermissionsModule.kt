package com.arttttt.permissions.impl.components.di

import com.arttttt.permissions.api.PermissionsComponent
import com.arttttt.permissions.impl.data.model.IgnoreBatteryOptimizationsPermission
import com.arttttt.permissions.impl.data.repository.PermissionsRepositoryImpl
import com.arttttt.permissions.impl.domain.entity.StandardPermission
import com.arttttt.permissions.impl.domain.repository.PermissionsRepository
import com.arttttt.permissions.impl.domain.store.PermissionsStore
import com.arttttt.permissions.impl.domain.store.PermissionsStoreFactory
import com.arttttt.permissions.impl.utils.PermissionsRequester
import com.arttttt.permissions.impl.utils.PermissionsRequesterImpl
import com.arttttt.permissions.impl.utils.handlers.IgnoreBatteryOptimizationsPermissionHandler
import com.arttttt.permissions.impl.utils.handlers.StandardPermissionHandler
import org.koin.dsl.module

internal val permissionsModule = module {

    scope<PermissionsComponent> {
        scoped<PermissionsRequester> {
            PermissionsRequesterImpl(
                activity = get(),
                handlers = mapOf(
                    StandardPermission::class to StandardPermissionHandler(),
                    IgnoreBatteryOptimizationsPermission::class to IgnoreBatteryOptimizationsPermissionHandler()
                )
            )
        }

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