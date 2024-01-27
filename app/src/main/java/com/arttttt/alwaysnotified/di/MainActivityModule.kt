package com.arttttt.alwaysnotified.di

import com.arttttt.alwaysnotified.MainActivity
import com.arttttt.alwaysnotified.data.model.IgnoreBatteryOptimizationsPermission
import com.arttttt.alwaysnotified.domain.entity.permission.StandardPermission
import com.arttttt.alwaysnotified.utils.permissions.PermissionsRequester
import com.arttttt.alwaysnotified.utils.permissions.handlers.IgnoreBatteryOptimizationsPermissionHandler
import com.arttttt.alwaysnotified.utils.permissions.PermissionsRequesterImpl
import com.arttttt.alwaysnotified.utils.permissions.handlers.StandardPermissionHandler
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