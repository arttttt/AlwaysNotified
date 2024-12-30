package com.arttttt.alwaysnotified

import android.Manifest
import android.annotation.SuppressLint
import android.app.Notification
import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.ServiceCompat
import com.arttttt.alwaysnotified.utils.extensions.isPermissionGranted

class AppsLaunchService : Service() {

    companion object {

        private const val NOTIFICATION_CHANNEL_ID = "apps_service_notifications"
        private const val NOTIFICATION_CHANNEL_NAME = "Apps service notifications"

        private const val NOTIFICATION_ID = 1

        private const val FOREGROUND_SERVICE_TYPE_ABSENT = 0
    }

    @get:SuppressLint("ObsoleteSdkInt")
    @get:Suppress("DEPRECATION")
    private val foregroundServiceTypeCompat: Int
        get() {
            return when {
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE -> ServiceInfo.FOREGROUND_SERVICE_TYPE_SPECIAL_USE
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q -> ServiceInfo.FOREGROUND_SERVICE_TYPE_NONE
                else -> FOREGROUND_SERVICE_TYPE_ABSENT
            }
        }

    override fun onCreate() {
        super.onCreate()

        if (isPermissionGranted(Manifest.permission.POST_NOTIFICATIONS)) {
            createNotificationChannel()

            showNotification()
        }

        stopService()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun createNotificationChannel() {
        val notificationManager = NotificationManagerCompat.from(applicationContext)

        if (notificationManager.getNotificationChannel(NOTIFICATION_CHANNEL_ID) != null) return

        val channel = NotificationChannelCompat
            .Builder(
                NOTIFICATION_CHANNEL_ID,
                NotificationManagerCompat.IMPORTANCE_HIGH,
            )
            .setName(NOTIFICATION_CHANNEL_NAME)
            .build()

        notificationManager.createNotificationChannel(channel)
    }

    private fun stopService() {
        ServiceCompat.stopForeground(this, ServiceCompat.STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    private fun showNotification() {
        startForeground(
            NOTIFICATION_ID,
            createNotification(),
            foregroundServiceTypeCompat,
        )
    }

    private fun createNotification(): Notification {
        return NotificationCompat
            .Builder(this, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setForegroundServiceBehavior(NotificationCompat.FOREGROUND_SERVICE_IMMEDIATE)
            .setContentTitle("Checking notifications")
            .setOngoing(true)
            .setAutoCancel(true)
            .setOnlyAlertOnce(true)
            .setProgress(0, 0, true)
            .build()
    }
}