package com.arttttt.appholder.data.model

import android.Manifest
import android.content.Context
import com.arttttt.appholder.domain.entity.permission.Permission2
import com.arttttt.appholder.domain.entity.permission.StandardPermission
import com.arttttt.appholder.utils.extensions.checkStatusImpl

data object QueryAllPackagesPermission: StandardPermission {

    override val title: String = "Query all packages"
    override val permission: String = Manifest.permission.QUERY_ALL_PACKAGES

    override fun checkStatus(context: Context): Permission2.Status {
        return checkStatusImpl(context)
    }
}