package com.arttttt.alwaysnotified.domain.repository

import com.arttttt.alwaysnotified.domain.entity.permission.Permission2

interface PermissionsRepository {

    fun getRequiredPermissions(): List<Permission2>

    fun checkPermission(permission: Permission2): Permission2.Status
}