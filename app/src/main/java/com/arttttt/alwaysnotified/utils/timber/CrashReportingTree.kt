package com.arttttt.alwaysnotified.utils.timber

import android.util.Log
import com.google.firebase.crashlytics.FirebaseCrashlytics
import timber.log.Timber

class CrashReportingTree(
    private val printLogToConsole: Boolean,
) : Timber.DebugTree() {
    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        if (printLogToConsole) {
            super.log(priority, tag, message, t)
        }

        val crashlytics = FirebaseCrashlytics.getInstance()
        crashlytics.log(message)

        if (priority == Log.ERROR) {
            if (t == null) {
                crashlytics.recordException(Throwable(message))
            } else {
                crashlytics.recordException(t)
            }
        }
        if (priority == Log.WARN) {
            crashlytics.log(message)
        }
    }
}