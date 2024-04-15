package com.arttttt.alwaysnotified.di

import com.arttttt.alwaysnotified.MainActivity
import com.arttttt.permissions.impl.data.model.IgnoreBatteryOptimizationsPermission
import com.arttttt.permissions.impl.domain.entity.StandardPermission
import com.arttttt.permissions.impl.utils.PermissionsRequester
import com.arttttt.permissions.impl.utils.handlers.IgnoreBatteryOptimizationsPermissionHandler
import com.arttttt.permissions.impl.utils.PermissionsRequesterImpl
import com.arttttt.permissions.impl.utils.handlers.StandardPermissionHandler
import org.koin.dsl.module

val mainActivityModule = module {
    scope<MainActivity> {
        scoped<PermissionsRequester> {
            PermissionsRequesterImpl(
                activity = get(),
                handlers = mapOf(
                    StandardPermission::class to StandardPermissionHandler(),
                    IgnoreBatteryOptimizationsPermission::class to IgnoreBatteryOptimizationsPermissionHandler()
                )
            )
        }
    }
}