package com.arttttt.appholder.di

import com.arttttt.appholder.MainActivity
import com.arttttt.appholder.data.model.IgnoreBatteryOptimizationsPermission
import com.arttttt.appholder.domain.entity.permission.StandardPermission
import com.arttttt.appholder.utils.IgnoreBatteryOptimizationsPermissionHandler
import com.arttttt.appholder.utils.PermissionsRequester
import com.arttttt.appholder.utils.StandardPermissionHandler
import org.koin.dsl.module

val mainActivityModule = module {
    scope<MainActivity> {
        scoped {
            PermissionsRequester(
                activity = get(),
                handlers = mapOf(
                    StandardPermission::class to StandardPermissionHandler(),
                    IgnoreBatteryOptimizationsPermission::class to IgnoreBatteryOptimizationsPermissionHandler()
                )
            )
        }
    }
}