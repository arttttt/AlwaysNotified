package com.arttttt.appholder.utils

import androidx.activity.ComponentActivity
import com.arttttt.appholder.castTo
import com.arttttt.appholder.domain.entity.permission.Permission2
import com.arttttt.appholder.domain.entity.permission.StandardPermission
import kotlin.reflect.KClass

class PermissionsRequester(
    private val activity: ComponentActivity,
    private val handlers: Map<KClass<out Permission2>, PermissionHandler<*>>,
) {

    suspend fun requestPermission(permission: Permission2): Permission2.Status {
        return when (permission) {
            is StandardPermission -> handlers.get(StandardPermission::class)
            else -> handlers.get(permission::class)
        }
            ?.castTo<PermissionHandler<Permission2>>()
            ?.requestPermission(activity, permission)
            ?: throw IllegalArgumentException("Permission handler not fount: $permission")
    }
}