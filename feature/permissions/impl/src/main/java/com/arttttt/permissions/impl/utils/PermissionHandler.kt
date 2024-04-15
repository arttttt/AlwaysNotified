package com.arttttt.permissions.impl.utils

import androidx.activity.ComponentActivity
import com.arttttt.permissions.api.Permission2

interface PermissionHandler<T : Permission2> {
    suspend fun requestPermission(
        activity: ComponentActivity,
        permission: T,
    ): Permission2.Status
}