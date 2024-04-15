package com.arttttt.permissions.impl.data.model

import android.Manifest
import android.content.Context
import com.arttttt.permissions.api.Permission2
import com.arttttt.permissions.impl.domain.entity.StandardPermission
import com.arttttt.permissions.impl.utils.checkStatusImpl

internal data object QueryAllPackagesPermission: StandardPermission {

    override val title: String = "Query all packages"
    override val permission: String = Manifest.permission.QUERY_ALL_PACKAGES

    override fun checkStatus(context: Context): Permission2.Status {
        return checkStatusImpl(context)
    }
}