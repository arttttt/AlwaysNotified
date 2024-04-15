package com.arttttt.permissions.impl.utils

import com.arttttt.permissions.api.Permission2

internal interface PermissionsRequester {

    suspend fun requestPermission(permission: Permission2): Permission2.Status
}