package com.arttttt.alwaysnotified.utils

import android.app.ActivityManager
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.os.SystemClock
import androidx.core.content.ContextCompat
import androidx.core.content.getSystemService
import com.arttttt.alwaysnotified.AppsLaunchService
import com.arttttt.alwaysnotified.utils.extensions.intent

/**
 * todo: more informative denial handling
 */
class AppsServiceManager(
    private val context: Context,
) {

    companion object {

        private const val APPS_SERVICE_REQUEST_CODE = 100

        private const val APPS_SERVICE_INTERVAL_MS = 1000
    }

    private val alarmManager by lazy {
        context.getSystemService<AlarmManager>()!!
    }

    fun launchAppsService() {
        ContextCompat.startForegroundService(context, context.intent<AppsLaunchService>())
    }

    fun scheduleAppServiceLaunch() {
        if (!alarmManager.canScheduleExactAlarms()) return

        if (isAlarmScheduled(context, APPS_SERVICE_REQUEST_CODE)) {
            cancelAlarm(
                context = context,
                alarmManager = alarmManager,
                requestCode = APPS_SERVICE_REQUEST_CODE,
            )
        }

        scheduleAlarm(
            context = context,
            alarmManager = alarmManager,
            requestCode = APPS_SERVICE_REQUEST_CODE,
        )
    }

    private fun scheduleAlarm(
        context: Context,
        alarmManager: AlarmManager,
        requestCode: Int,
    ) {
        val intent = context.intent<AppsLaunchService>()
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT,
        )

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.ELAPSED_REALTIME_WAKEUP,
            SystemClock.elapsedRealtime() + APPS_SERVICE_INTERVAL_MS,
            pendingIntent
        )
    }

    private fun cancelAlarm(
        context: Context,
        alarmManager: AlarmManager,
        requestCode: Int,
    ) {
        val intent = context.intent<AppsLaunchService>()
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT,
        )

        alarmManager.cancel(pendingIntent)
    }

    private fun isAlarmScheduled(
        context: Context,
        requestCode: Int,
    ): Boolean {
        val intent = context.intent<AppsLaunchService>()
        val pendingIntent = PendingIntent.getService(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE,
        )
        return pendingIntent != null
    }

    private fun isServiceRunning(context: Context, serviceClass: Class<*>): Boolean {
        val activityManager = context.getSystemService<ActivityManager>()!!

        val runningServices = activityManager.getRunningServices(Int.MAX_VALUE)

        for (service in runningServices) {
            if (serviceClass.name == service.service.className) {
                return true
            }
        }
        return false
    }
}