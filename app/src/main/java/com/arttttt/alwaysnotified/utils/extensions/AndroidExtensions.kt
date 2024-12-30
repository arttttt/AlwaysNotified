package com.arttttt.alwaysnotified.utils.extensions

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

inline fun <reified T> Context.intent(
    block: Intent.() -> Unit = {}
): Intent {
    return Intent(this, T::class.java).apply(block)
}

fun Context.isPermissionGranted(permission: String): Boolean {
    return ContextCompat.checkSelfPermission(
        this,
        permission,
    ) == PackageManager.PERMISSION_GRANTED
}