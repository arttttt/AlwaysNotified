package com.arttttt.appholder

import android.app.Notification
import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import kotlinx.coroutines.runBlocking
import org.koin.android.ext.android.inject

class HolderService : Service() {

    companion object {

        private const val NOTIFICATION_CHANNEL_ID = "holder_service_notifications"
        private const val NOTIFICATION_CHANNEL_NAME = "Holder service notifications"

        private const val NOTIFICATION_ID = 1
    }

    private val appsLauncher by inject<AppsLauncher>()

    override fun onCreate() {
        super.onCreate()

        createNotificationChannel()

        startForeground(
            NOTIFICATION_ID,
            createNotification(),
        )

        runBlocking {
            appsLauncher.launchApps()
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun createNotification(): Notification {

        return NotificationCompat
            .Builder(this, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
            .setContentTitle("AppHolder service is running")
            .build()
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannelCompat
            .Builder(
                NOTIFICATION_CHANNEL_ID,
                NotificationManagerCompat.IMPORTANCE_HIGH,
            )
            .setName(NOTIFICATION_CHANNEL_NAME)
            .build()

        NotificationManagerCompat
            .from(applicationContext)
            .createNotificationChannel(channel)
    }
}