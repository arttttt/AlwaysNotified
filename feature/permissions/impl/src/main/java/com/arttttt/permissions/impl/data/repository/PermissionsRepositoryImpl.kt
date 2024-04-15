package com.arttttt.permissions.impl.data.repository

import android.content.Context
import com.arttttt.permissions.api.Permission2
import com.arttttt.permissions.impl.data.model.PostNotificationsPermission
import com.arttttt.permissions.impl.domain.repository.PermissionsRepository

class PermissionsRepositoryImpl(
    private val context: Context
) : PermissionsRepository {

    private val permissions: List<Permission2> = listOf(
        PostNotificationsPermission,
        //QueryAllPackagesPermission,
        //IgnoreBatteryOptimizationsPermission,
    )

    override fun getRequiredPermissions(): List<Permission2> {
        return permissions
    }

    override fun checkPermission(permission: Permission2): Permission2.Status {
        return permission.checkStatus(context)
    }
}