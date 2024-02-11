package com.arttttt.alwaysnotified.data.repository

import android.content.Context
import com.arttttt.alwaysnotified.data.model.IgnoreBatteryOptimizationsPermission
import com.arttttt.alwaysnotified.data.model.PostNotificationsPermission
import com.arttttt.alwaysnotified.data.model.QueryAllPackagesPermission
import com.arttttt.alwaysnotified.domain.entity.permission.Permission2
import com.arttttt.alwaysnotified.domain.repository.PermissionsRepository

class PermissionsRepositoryImpl(
    private val context: Context
) : PermissionsRepository {

    private val permissions: List<Permission2> = listOf(
        //PostNotificationsPermission,
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