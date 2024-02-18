package com.arttttt.alwaysnotified

import android.Manifest
import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.os.IBinder
import android.os.RemoteException
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.arttttt.alwaysnotified.utils.ipc.AppsServiceIpcMessenger

class AppStartupService : Service() {

    companion object {

        private const val NOTIFICATION_CHANNEL_ID = "holder_service_notifications"
        private const val NOTIFICATION_CHANNEL_NAME = "Holder service notifications"

        private const val NOTIFICATION_ID = 1

        private const val LAUNCH_NEXT_ACTION = "launch_next_action"
        private const val STOP_CHAIN_ACTION = "stop_chain_action"
    }

    private val messenger by lazy {
        AppsServiceIpcMessenger(
            tag = "service",
            onMessageReceived = ::handleMessage,
        )
    }

    override fun onBind(intent: Intent?): IBinder {
        return messenger.binder
    }

    override fun onCreate() {
        super.onCreate()

        if (ActivityCompat.checkSelfPermission(applicationContext, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            stopSelf()
        } else {
            createNotificationChannel()

            NotificationManagerCompat
                .from(applicationContext)
                .notify(
                    NOTIFICATION_ID,
                    createNotification(),
                )
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        handleAction(intent?.action)

        return START_STICKY
    }

    private fun handleMessage(message: AppsServiceIpcMessenger.IpcMessage) {
        when (message) {
            is AppsServiceIpcMessenger.IpcMessage.StopService -> stopService()
            else -> {}
        }
    }

    private fun handleAction(action: String?) {
        when (action) {
            LAUNCH_NEXT_ACTION -> trySendMessageToClient(AppsServiceIpcMessenger.IpcMessage.LaunchNext)
            STOP_CHAIN_ACTION -> trySendMessageToClient(AppsServiceIpcMessenger.IpcMessage.StopChain)
        }
    }

    private fun createNotification(): Notification {
        return NotificationCompat
            .Builder(this, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
            .setForegroundServiceBehavior(NotificationCompat.FOREGROUND_SERVICE_IMMEDIATE)
            .setContentTitle("Always notified")
            .addAction(
                NotificationCompat.Action
                    .Builder(
                        null,
                        "Launch next",
                        getServiceIntent(LAUNCH_NEXT_ACTION),
                    )
                    .build()
            )
            .addAction(
                NotificationCompat.Action
                    .Builder(
                        null,
                        "Stop chain",
                        getServiceIntent(STOP_CHAIN_ACTION),
                    )
                    .build()
            )
            .setOngoing(true)
            .setAutoCancel(true)
            .build()
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

    private fun getServiceIntent(action: String): PendingIntent {
        val intent = Intent(
            applicationContext,
            AppStartupService::class.java,
        )

        intent.action = action

        return PendingIntent.getService(
            applicationContext,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE,
        )
    }

    private fun trySendMessageToClient(message: AppsServiceIpcMessenger.IpcMessage) {
        try {
            messenger.sendMessage(message)
        } catch (_: RemoteException) {
            stopService()
        }
    }

    private fun stopService() {
        stopSelf()
        NotificationManagerCompat.from(applicationContext).cancel(NOTIFICATION_ID)
    }
}