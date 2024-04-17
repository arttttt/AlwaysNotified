package com.arttttt.alwaysnotified

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import org.koin.core.component.KoinComponent

class BootReceiver : BroadcastReceiver(), KoinComponent {

    companion object {

        private const val NOTIFICATION_CHANNEL_ID = "holder_service_notifications"
        private const val NOTIFICATION_CHANNEL_NAME = "Holder service notifications"

        private const val NOTIFICATION_ID = 1
    }

    //private val appsLauncher: AppsLauncher by inject()

    override fun onReceive(context: Context, intent: Intent) {
        /*Log.e(
            "TEST",
            """
                trying to start all the apps
                action = ${intent.action}
            """.trimIndent()
        )

        if (intent.action != Intent.ACTION_BOOT_COMPLETED) return

        appsLauncher.startAppStartupService()*/

        /*createNotificationChannel(context)

        val payload = runBlocking {
            appsLauncher.getLaunchIntent()
        }

        Log.e(
            "TEST",
            """
                server started
                payload = $payload
            """.trimIndent()
        )

        if (context.checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_DENIED) return

        NotificationManagerCompat
            .from(context.applicationContext)
            .notify(
                NOTIFICATION_ID,
                createNotification(context, payload)
            )*/
    }

    /*private fun createNotification(
        context: Context,
        payload: Intent?
    ): Notification {

        return NotificationCompat
            .Builder(context, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
            .setContentTitle("Always notified")
            .setContentText("Tap the notification to start selected apps")
            .setContentIntent(
                PendingIntent.getActivity(
                    context.applicationContext,
                    0,
                    payload,
                    PendingIntent.FLAG_IMMUTABLE,
                )
            )
            .setAutoCancel(true)
            .build()
    }

    private fun createNotificationChannel(context: Context) {
        val channel = NotificationChannelCompat
            .Builder(
                NOTIFICATION_CHANNEL_ID,
                NotificationManagerCompat.IMPORTANCE_HIGH,
            )
            .setName(NOTIFICATION_CHANNEL_NAME)
            .build()

        NotificationManagerCompat
            .from(context.applicationContext)
            .createNotificationChannel(channel)
    }*/
}