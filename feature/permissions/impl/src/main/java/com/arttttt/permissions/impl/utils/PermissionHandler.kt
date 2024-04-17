package com.arttttt.permissions.impl.utils

import androidx.activity.ComponentActivity
import com.arttttt.permissions.impl.domain.entity.Permission2

internal interface PermissionHandler<T : Permission2> {
    suspend fun requestPermission(
        activity: ComponentActivity,
        permission: T,
    ): Permission2.Status
}