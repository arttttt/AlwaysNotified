package com.arttttt.appholder.domain.repository

import com.arttttt.appholder.domain.entity.permission.Permission2

interface PermissionsRepository {

    fun getRequiredPermissions(): List<Permission2>

    fun checkPermission(permission: Permission2): Permission2.Status
}