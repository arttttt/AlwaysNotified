package com.arttttt.appholder.utils

import android.content.Context
import android.os.PowerManager
import androidx.activity.ComponentActivity
import androidx.core.content.getSystemService
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.arttttt.appholder.data.model.IgnoreBatteryOptimizationsPermission
import com.arttttt.appholder.domain.entity.permission.Permission2
import com.arttttt.appholder.of
import kotlinx.coroutines.channels.Channel

class IgnoreBatteryOptimizationsPermissionHandler : PermissionHandler<IgnoreBatteryOptimizationsPermission> {

    private val lifecycleObserver = object : DefaultLifecycleObserver {
        private var skipNextResume = false
        private var resumeChannel: Channel<Unit>? = null

        fun init(lifecycle: Lifecycle) {
            if (lifecycle.currentState == Lifecycle.State.RESUMED) {
                skipNextResume = true
            }

            resumeChannel = Channel(Channel.CONFLATED)
        }

        fun clear() {
            resumeChannel?.close()
            resumeChannel = null
        }

        override fun onResume(owner: LifecycleOwner) {
            if (skipNextResume) {
                skipNextResume = false
            } else {
                resumeChannel?.trySend(Unit)
            }
        }

        suspend fun awaitResume() {
            resumeChannel?.receive()
        }
    }

    override suspend fun requestPermission(
        activity: ComponentActivity,
        permission: IgnoreBatteryOptimizationsPermission
    ): Permission2.Status {
        lifecycleObserver.init(activity.lifecycle)
        activity.lifecycle.addObserver(lifecycleObserver)

        val intent = permission.createIntent(activity)

        return try {
            if (intent.resolveActivity(activity.applicationContext.packageManager) != null) {
                activity.startActivity(intent)
                lifecycleObserver.awaitResume()
            }

            activity.applicationContext.checkPermissionStatus()
        } finally {
            lifecycleObserver.clear()
            activity.lifecycle.removeObserver(lifecycleObserver)
        }
    }

    private fun Context.checkPermissionStatus(): Permission2.Status {
        val powerManager = getSystemService<PowerManager>()!!
        return Permission2.Status.of(powerManager.isIgnoringBatteryOptimizations(packageName))
    }
}
