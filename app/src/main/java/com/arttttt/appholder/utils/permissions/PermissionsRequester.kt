package com.arttttt.appholder.utils.permissions

import com.arttttt.appholder.domain.entity.permission.Permission2

interface PermissionsRequester {

    suspend fun requestPermission(permission: Permission2): Permission2.Status
}