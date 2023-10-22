package com.arttttt.appholder.di

import com.arttttt.appholder.MainActivity
import com.arttttt.appholder.data.model.IgnoreBatteryOptimizationsPermission
import com.arttttt.appholder.domain.entity.permission.StandardPermission
import com.arttttt.appholder.utils.permissions.PermissionsRequester
import com.arttttt.appholder.utils.permissions.handlers.IgnoreBatteryOptimizationsPermissionHandler
import com.arttttt.appholder.utils.permissions.PermissionsRequesterImpl
import com.arttttt.appholder.utils.permissions.handlers.StandardPermissionHandler
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