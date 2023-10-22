package com.arttttt.appholder.utils.extensions

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Parcelable
import androidx.core.content.IntentCompat
import java.io.Serializable

inline fun <reified T> Context.intent(
    block: Intent.() -> Unit = {}
): Intent {
    return Intent(this, T::class.java).apply(block)
}

inline fun <reified T : Parcelable?> Intent.getParcelable(name: String): T {
    return IntentCompat.getParcelableExtra(
        this,
        name,
        T::class.java,
    ) as T
}

inline fun <reified T : Serializable?> Intent.getSerializable(name: String): T {
    return if (Build.VERSION.SDK_INT >= 34) {
        getSerializableExtra(name, T::class.java) as T
    } else {
        getSerializableExtra(name) as T
    }
}