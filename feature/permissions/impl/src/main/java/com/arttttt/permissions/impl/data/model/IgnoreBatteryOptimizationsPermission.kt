package com.arttttt.permissions.impl.data.model

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.PowerManager
import android.provider.Settings
import androidx.core.content.getSystemService
import com.arttttt.permissions.impl.domain.entity.Permission2
import com.arttttt.permissions.impl.domain.entity.IntentPermission
import com.arttttt.permissions.impl.utils.of

@SuppressLint("BatteryLife")
internal data object IgnoreBatteryOptimizationsPermission : IntentPermission {

    override val title: String = "Ignore battery optimizations"

    override fun createIntent(context: Context): Intent {
        return Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS).apply {
            data = Uri.fromParts("package", context.packageName, null)
        }
    }

    override fun checkStatus(context: Context): Permission2.Status {
        val powerManager = context.getSystemService<PowerManager>()!!
        return Permission2.Status.of(powerManager.isIgnoringBatteryOptimizations(context.packageName))
    }
}