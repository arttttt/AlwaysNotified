package com.arttttt.appholder.utils.permissions

import androidx.activity.ComponentActivity
import com.arttttt.appholder.domain.entity.permission.Permission2
import com.arttttt.appholder.domain.entity.permission.StandardPermission
import com.arttttt.appholder.utils.extensions.castTo
import kotlin.reflect.KClass

class PermissionsRequesterImpl(
    private val activity: ComponentActivity,
    private val handlers: Map<KClass<out Permission2>, PermissionHandler<*>>,
) : PermissionsRequester {

    override suspend fun requestPermission(permission: Permission2): Permission2.Status {
        return when (permission) {
            is StandardPermission -> handlers.get(StandardPermission::class)
            else -> handlers.get(permission::class)
        }
            ?.castTo<PermissionHandler<Permission2>>()
            ?.requestPermission(activity, permission)
            ?: throw IllegalArgumentException("Permission handler not fount: $permission")
    }
}