package com.arttttt.permissions.impl.domain.entity

import android.content.Context

internal interface Permission2 {

    sealed class Status {

        companion object;

        data object Granted : Status()
        data object Denied : Status()
    }

    val title: String

    fun checkStatus(context: Context): Status
}