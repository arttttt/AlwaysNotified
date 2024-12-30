package com.arttttt.permissions.impl.data.model

import android.Manifest
import com.arttttt.permissions.impl.domain.entity.StandardPermission

internal data object PostNotificationsPermission : StandardPermission {

    override val title: String = "Post notifications"
    override val permission: String = Manifest.permission.POST_NOTIFICATIONS
}