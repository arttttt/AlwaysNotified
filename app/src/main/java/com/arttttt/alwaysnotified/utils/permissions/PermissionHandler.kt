package com.arttttt.alwaysnotified.utils.permissions

import androidx.activity.ComponentActivity
import com.arttttt.alwaysnotified.domain.entity.permission.Permission2

interface PermissionHandler<T : Permission2> {
    suspend fun requestPermission(
        activity: ComponentActivity,
        permission: T,
    ): Permission2.Status
}