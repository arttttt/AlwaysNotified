package com.arttttt.permissions.impl.utils

import androidx.activity.ComponentActivity
import com.arttttt.utils.castTo
import com.arttttt.permissions.impl.domain.entity.Permission2
import com.arttttt.permissions.impl.domain.entity.StandardPermission
import kotlin.reflect.KClass

internal class PermissionsRequesterImpl(
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