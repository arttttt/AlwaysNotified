package com.arttttt.alwaysnotified.data.model

import android.Manifest
import android.content.Context
import com.arttttt.alwaysnotified.domain.entity.permission.Permission2
import com.arttttt.alwaysnotified.domain.entity.permission.StandardPermission
import com.arttttt.alwaysnotified.utils.extensions.checkStatusImpl

data object QueryAllPackagesPermission: StandardPermission {

    override val title: String = "Query all packages"
    override val permission: String = Manifest.permission.QUERY_ALL_PACKAGES

    override fun checkStatus(context: Context): Permission2.Status {
        return checkStatusImpl(context)
    }
}