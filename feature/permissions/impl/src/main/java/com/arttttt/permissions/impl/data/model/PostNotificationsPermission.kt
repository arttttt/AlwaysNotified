package com.arttttt.permissions.impl.data.model

import android.Manifest
import android.content.Context
import com.arttttt.permissions.impl.domain.entity.Permission2
import com.arttttt.permissions.impl.domain.entity.StandardPermission
import com.arttttt.permissions.impl.utils.checkStatusImpl

internal data object PostNotificationsPermission : StandardPermission {

    override val title: String = "Post notifications"
    override val permission: String = Manifest.permission.POST_NOTIFICATIONS

    override fun checkStatus(context: Context): Permission2.Status {
        return checkStatusImpl(context)
    }
}