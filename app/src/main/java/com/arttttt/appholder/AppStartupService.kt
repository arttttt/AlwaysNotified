package com.arttttt.appholder

import android.Manifest
import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.os.IBinder
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class AppStartupService : Service() {

    companion object {

        private const val NOTIFICATION_CHANNEL_ID = "holder_service_notifications"
        private const val NOTIFICATION_CHANNEL_NAME = "Holder service notifications"

        private const val NOTIFICATION_ID = 1
    }

    private val appsLauncher by inject<AppsLauncher>()

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate() {
        super.onCreate()

        GlobalScope.launch {
            createNotificationChannel()

            val payload = appsLauncher.getLaunchIntent()

            if (ActivityCompat.checkSelfPermission(applicationContext, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                return@launch
            }

            NotificationManagerCompat
                .from(applicationContext)
                .notify(
                    NOTIFICATION_ID,
                    createNotification(payload),
                )
        }
    }

    private fun createNotification(payload: Intent?): Notification {
        return NotificationCompat
            .Builder(this, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
            .setContentTitle("App holder")
            .setContentText("Tap the notification to launch the selected apps")
            .setContentIntent(
                PendingIntent.getActivity(
                    applicationContext,
                    0,
                    payload,
                    PendingIntent.FLAG_IMMUTABLE,
                )
            )
            .setOngoing(true)
            .setAutoCancel(true)
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