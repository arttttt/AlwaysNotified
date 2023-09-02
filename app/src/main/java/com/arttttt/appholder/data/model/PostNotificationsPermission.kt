package com.arttttt.appholder.data.model

import android.Manifest
import android.content.Context
import com.arttttt.appholder.checkStatusImpl
import com.arttttt.appholder.domain.entity.permission.Permission2
import com.arttttt.appholder.domain.entity.permission.StandardPermission

data object PostNotificationsPermission : StandardPermission {

    override val title: String = "Post notifications"
    override val permission: String = Manifest.permission.POST_NOTIFICATIONS

    override fun checkStatus(context: Context): Permission2.Status {
        return checkStatusImpl(context)
    }
}