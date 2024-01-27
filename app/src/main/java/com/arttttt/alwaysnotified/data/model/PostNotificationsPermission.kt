package com.arttttt.alwaysnotified.data.model

import android.Manifest
import android.content.Context
import com.arttttt.alwaysnotified.domain.entity.permission.Permission2
import com.arttttt.alwaysnotified.domain.entity.permission.StandardPermission
import com.arttttt.alwaysnotified.utils.extensions.checkStatusImpl

data object PostNotificationsPermission : StandardPermission {

    override val title: String = "Post notifications"
    override val permission: String = Manifest.permission.POST_NOTIFICATIONS

    override fun checkStatus(context: Context): Permission2.Status {
        return checkStatusImpl(context)
    }
}