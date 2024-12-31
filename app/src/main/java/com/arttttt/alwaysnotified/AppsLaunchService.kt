package com.arttttt.alwaysnotified

import android.Manifest
import android.annotation.SuppressLint
import android.app.Notification
import android.app.Service
import android.content.ComponentName
import android.content.Intent
import android.content.pm.ServiceInfo
import android.net.Uri
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.ServiceCompat
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.Lifecycle
import com.arkivanov.essenty.lifecycle.LifecycleOwner
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.destroy
import com.arkivanov.essenty.lifecycle.doOnDestroy
import com.arkivanov.essenty.lifecycle.resume
import com.arttttt.alwaysnotified.utils.AppsServiceManager
import com.arttttt.alwaysnotified.utils.extensions.isPermissionGranted
import com.arttttt.appslist.api.AppInfo
import com.arttttt.appslist.api.AppsListComponent
import com.arttttt.core.arch.context.wrapComponentContext
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.timeout
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import timber.log.Timber
import java.util.LinkedList
import java.util.Queue
import kotlin.time.Duration.Companion.seconds

/**
 * todo: don't use component here
 */
class AppsLaunchService : Service(), LifecycleOwner {

    companion object {

        private const val NOTIFICATION_CHANNEL_ID = "apps_service_notifications"
        private const val NOTIFICATION_CHANNEL_NAME = "Apps service notifications"

        private const val NOTIFICATION_ID = 1

        private const val FOREGROUND_SERVICE_TYPE_ABSENT = 0

        private const val TAG = "AppsLaunchService"
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

    private val lifecycleRegistry: LifecycleRegistry = LifecycleRegistry()
    override val lifecycle: Lifecycle = lifecycleRegistry

    private val appsServiceManager: AppsServiceManager by inject()
    private val appsListComponentFactory: AppsListComponent.Factory by inject()

    @OptIn(FlowPreview::class)
    override fun onCreate() {
        super.onCreate()

        lifecycleRegistry.resume()

        if (isPermissionGranted(Manifest.permission.POST_NOTIFICATIONS)) {
            createNotificationChannel()

            showNotification()

            val coroutineScope = MainScope()

            lifecycle.doOnDestroy {
                coroutineScope.cancel()
            }

            val appsListComponent = appsListComponentFactory.create(
                context = wrapComponentContext(
                    context = DefaultComponentContext(
                        lifecycle = lifecycle,
                    ),
                    parentScopeID = null,
                ),
                startApps = {},
            )

            coroutineScope.launch(NonCancellable) {
                val selectedApps = appsListComponent
                    .states
                    .filter { state -> !state.isLoading && state.selectedApps.isNotEmpty() }
                    .timeout(5.seconds)
                    .first()
                    .selectedApps

                Timber.tag(TAG).d(
                    buildString {
                        appendLine("selectedApps")
                        append(selectedApps.joinToString("\n"))
                    }
                )

                selectedApps.forEach { appInfo ->
                    var error: Exception? = null

                    val components: Queue<AppInfo.Component> = LinkedList(appInfo.components.sorted())
                    do {
                        try {
                            val component = components.poll()

                            Timber.tag(TAG).d("Trying to launch $component")

                            when (component) {
                                is AppInfo.Component.Service -> {
                                    startService(
                                        Intent().apply {
                                            this.component = ComponentName(
                                                component.pkg,
                                                component.name,
                                            )
                                        },
                                    )
                                }
                                is AppInfo.Component.ContentProvider -> {
                                    val authority = component.authority
                                    val uri = Uri.parse("content://$authority")

                                    contentResolver
                                        .query(uri, null, null, null, null)
                                        ?.close()
                                }
                            }

                            delay(300)
                            error = null
                        } catch (e: Exception) {
                            Timber.tag(TAG).d(e)
                            error = e
                        }
                    } while (error != null && components.isNotEmpty())
                }

                appsServiceManager.scheduleAppServiceLaunch()
                stopService()
            }
        } else {
            stopService()
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()

        lifecycleRegistry.destroy()
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