package com.arttttt.appholder.utils.extensions

import kotlinx.coroutines.CancellationException

inline fun <reified R> Any.castTo(): R? {
    return this as? R
}

inline fun <reified R> Any.unsafeCastTo(): R {
    return this as R
}

inline fun <T> Result<T>.finally(block: () -> Unit): Result<T> {
    return try {
        this
    } finally {
        block.invoke()
    }
}

inline fun <T> Result<T>.exceptCancellationException(): Result<T> {
    return onFailure { e ->
        if (e is CancellationException) throw e
    }
}