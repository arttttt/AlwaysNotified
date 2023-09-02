package com.arttttt.appholder.data

import android.content.Context
import com.arttttt.appholder.data.model.IgnoreBatteryOptimizationsPermission
import com.arttttt.appholder.data.model.PostNotificationsPermission
import com.arttttt.appholder.data.model.QueryAllPackagesPermission
import com.arttttt.appholder.domain.entity.permission.Permission2
import com.arttttt.appholder.domain.repository.PermissionsRepository

class PermissionsRepositoryImpl(
    private val context: Context
) : PermissionsRepository {

    private val permissions = listOf(
        PostNotificationsPermission,
        QueryAllPackagesPermission,
        IgnoreBatteryOptimizationsPermission,
    )

    override fun getRequiredPermissions(): List<Permission2> {
        return permissions
    }

    override fun checkPermission(permission: Permission2): Permission2.Status {
        return permission.checkStatus(context)
    }
}