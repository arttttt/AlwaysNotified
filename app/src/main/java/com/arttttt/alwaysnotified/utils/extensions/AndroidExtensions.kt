package com.arttttt.alwaysnotified.utils.extensions

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Parcelable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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
        @Suppress("DEPRECATION")
        getSerializableExtra(name) as T
    }
}

@Composable
fun <T> rememberLambda(block: () -> T): () -> T {
    return remember {
        block
    }
}