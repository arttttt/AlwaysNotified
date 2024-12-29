package com.arttttt.permissions.impl.data.model

import android.app.AlarmManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.core.app.AlarmManagerCompat
import androidx.core.content.getSystemService
import com.arttttt.permissions.impl.domain.entity.IntentPermission
import com.arttttt.permissions.impl.domain.entity.Permission2
import com.arttttt.permissions.impl.utils.of

internal object ScheduleExactAlarmPermission : IntentPermission {

    override val title: String = "Schedule exact alarm"

    override fun createIntent(context: Context): Intent {
        return Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM).apply {
            data = Uri.fromParts("package", context.packageName, null)
        }
    }

    override fun checkStatus(context: Context): Permission2.Status {
        val alarmManager = context.getSystemService<AlarmManager>()!!
        return Permission2.Status.of(AlarmManagerCompat.canScheduleExactAlarms(alarmManager))
    }
}