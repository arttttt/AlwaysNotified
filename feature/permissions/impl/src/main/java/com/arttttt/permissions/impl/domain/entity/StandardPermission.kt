package com.arttttt.permissions.impl.domain.entity

import android.content.Context
import com.arttttt.permissions.impl.utils.checkStatusImpl

internal interface StandardPermission : Permission2 {

    val permission: String

    override fun checkStatus(context: Context): Permission2.Status {
        return checkStatusImpl(context)
    }
}