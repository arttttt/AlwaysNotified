package com.arttttt.appholder.utils

import androidx.activity.ComponentActivity
import com.arttttt.appholder.domain.entity.permission.Permission2

interface PermissionHandler<T : Permission2> {
    suspend fun requestPermission(
        activity: ComponentActivity,
        permission: T,
    ): Permission2.Status
}