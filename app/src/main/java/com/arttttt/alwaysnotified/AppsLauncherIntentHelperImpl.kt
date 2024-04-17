package com.arttttt.alwaysnotified

import android.content.Context
import android.content.Intent
import com.arttttt.alwaysnotified.utils.extensions.intent
import com.arttttt.appslist.api.AppsLauncherIntentHelper

class AppsLauncherIntentHelperImpl : AppsLauncherIntentHelper {

    override fun putExtra(
        intent: Intent,
        activityInfo: ActivityInfo,
        selectedActivities: Map<String, SelectedActivity>,
    ) {
        intent.putExtra(
            HolderActivity.TARGET_TITLE,
            activityInfo.name.substringAfterLast(".")
        )

        intent.putExtra(
            HolderActivity.MANUAL_MODE,
            selectedActivities[activityInfo.pkg]?.manualMode
        )
    }

    override fun createHolderIntent(
        context: Context,
        payload: ArrayList<Intent>,
    ): Intent {
        return context.intent<HolderActivity> {
            setClassName(
                context.packageName,
                HolderActivity::class.java.name
            )

            putExtra(
                HolderActivity.APPS_TO_START,
                payload,
            )

            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_NEW_DOCUMENT
        }
    }
}