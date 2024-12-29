package com.arttttt.permissions.impl.utils

import androidx.activity.ComponentActivity
import com.arttttt.permissions.impl.domain.entity.IntentPermission
import com.arttttt.utils.castTo
import com.arttttt.permissions.impl.domain.entity.Permission2
import com.arttttt.permissions.impl.domain.entity.StandardPermission
import com.arttttt.permissions.impl.utils.handlers.CommonIntentPermissionHandler
import com.arttttt.permissions.impl.utils.handlers.StandardPermissionHandler
import kotlin.reflect.KClass

internal class PermissionsRequesterImpl(
    private val activity: ComponentActivity,
    private val customHandlers: Map<KClass<out Permission2>, PermissionHandler<*>>,
) : PermissionsRequester {

    override suspend fun requestPermission(permission: Permission2): Permission2.Status {
        return when (permission) {
            is StandardPermission -> StandardPermissionHandler
            is IntentPermission -> CommonIntentPermissionHandler
            else -> customHandlers.get(permission::class)
        }
            ?.castTo<PermissionHandler<Permission2>>()
            ?.requestPermission(activity, permission)
            ?: throw IllegalArgumentException("Permission handler not fount: $permission")
    }
}