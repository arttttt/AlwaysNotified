package com.arttttt.alwaysnotified.utils.permissions

import com.arttttt.alwaysnotified.domain.entity.permission.Permission2

interface PermissionsRequester {

    suspend fun requestPermission(permission: Permission2): Permission2.Status
}