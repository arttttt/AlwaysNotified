package com.arttttt.permissions.impl.data.model

import android.Manifest
import com.arttttt.permissions.impl.domain.entity.StandardPermission

internal data object QueryAllPackagesPermission: StandardPermission {

    override val title: String = "Query all packages"
    override val permission: String = Manifest.permission.QUERY_ALL_PACKAGES
}