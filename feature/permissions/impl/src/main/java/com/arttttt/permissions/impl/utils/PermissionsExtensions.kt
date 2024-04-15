package com.arttttt.permissions.impl.utils

import android.content.Context
import android.content.pm.PackageManager
import com.arttttt.permissions.api.Permission2
import com.arttttt.permissions.impl.domain.entity.StandardPermission

internal fun Permission2.Status.Companion.of(value: Int): Permission2.Status {
    return if (value == PackageManager.PERMISSION_GRANTED) {
        Permission2.Status.Granted
    } else {
        Permission2.Status.Denied
    }
}

internal fun Permission2.Status.Companion.of(value: Boolean): Permission2.Status {
    return if (value) {
        Permission2.Status.Granted
    } else {
        Permission2.Status.Denied
    }
}

context(StandardPermission)
internal fun checkStatusImpl(context: Context): Permission2.Status {
    return Permission2.Status.of(context.checkSelfPermission(permission))
}