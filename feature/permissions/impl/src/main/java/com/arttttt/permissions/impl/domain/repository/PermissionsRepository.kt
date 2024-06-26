package com.arttttt.permissions.impl.domain.repository

import com.arttttt.permissions.impl.domain.entity.Permission2

internal interface PermissionsRepository {

    fun getRequiredPermissions(): List<Permission2>

    fun checkPermission(permission: Permission2): Permission2.Status
}