package com.arttttt.alwaysnotified

import android.Manifest
import android.annotation.SuppressLint
import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import android.os.RemoteException
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.ServiceCompat
import com.arttttt.alwaysnotified.utils.ipc.AppsServiceIpcMessenger

class AppStartupService : Service() {

    companion object {

        private const val NOTIFICATION_CHANNEL_ID = "holder_service_notifications"
        private const val NOTIFICATION_CHANNEL_NAME = "Holder service notifications"

        private const val NOTIFICATION_ID = 1

        private const val LAUNCH_NEXT_ACTION = "launch_next_action"
        private const val STOP_CHAIN_ACTION = "stop_chain_action"
        private const val STOP_SELF_ACTION = "stop_self_action"

        private const val FOREGROUND_SERVICE_TYPE_ABSENT = 0
    }

    private val messenger by lazy {
        AppsServiceIpcMessenger(
            tag = "service",
            onMessageReceived = ::handleMessage,
        )
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

    override fun onBind(intent: Intent?): IBinder {
        return messenger.binder
    }

    override fun onCreate() {
        super.onCreate()

        if (ActivityCompat.checkSelfPermission(applicationContext, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            stopSelf()
        } else {
            createNotificationChannel()

            showNotification(
                isActionAvailable = false
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
            is AppsServiceIpcMessenger.IpcMessage.HideLaunchButton -> {
                showNotification(
                    isActionAvailable = false
                )
            }
            is AppsServiceIpcMessenger.IpcMessage.ShowLaunchButton -> {
                showNotification(
                    isActionAvailable = true
                )
            }
            else -> {}
        }
    }

    private fun handleAction(action: String?) {
        when (action) {
            LAUNCH_NEXT_ACTION -> trySendMessageToClient(AppsServiceIpcMessenger.IpcMessage.LaunchNext)
            STOP_CHAIN_ACTION -> trySendMessageToClient(AppsServiceIpcMessenger.IpcMessage.StopChain)
            STOP_SELF_ACTION -> stopService()
        }
    }

    private fun createNotification(
        isActionAvailable: Boolean,
    ): Notification {
        return NotificationCompat
            .Builder(this, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
            .setForegroundServiceBehavior(NotificationCompat.FOREGROUND_SERVICE_IMMEDIATE)
            .setContentTitle("Always notified")
            .run {
                if (isActionAvailable) {
                    addAction(
                        NotificationCompat.Action
                            .Builder(
                                null,
                                "Launch next",
                                getServiceIntent(LAUNCH_NEXT_ACTION),
                            )
                            .build()
                    )
                } else {
                    this
                }
            }
            .setDeleteIntent(
                getServiceIntent(STOP_SELF_ACTION)
            )
            .setOngoing(true)
            .setAutoCancel(true)
            .setOnlyAlertOnce(true)
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
        ServiceCompat.stopForeground(this, ServiceCompat.STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    private fun showNotification(
        isActionAvailable: Boolean,
    ) {
        startForeground(
            NOTIFICATION_ID,
            createNotification(isActionAvailable),
            foregroundServiceTypeCompat,
        )
    }
}