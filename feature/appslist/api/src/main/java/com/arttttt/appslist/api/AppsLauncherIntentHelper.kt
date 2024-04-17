package com.arttttt.appslist.api

import android.content.Context
import android.content.Intent
import com.arttttt.alwaysnotified.ActivityInfo
import com.arttttt.alwaysnotified.SelectedActivity

interface AppsLauncherIntentHelper {

    fun putExtra(
        intent: Intent,
        activityInfo: ActivityInfo,
        selectedActivities: Map<String, SelectedActivity>,
    )

    fun createHolderIntent(
        context: Context,
        payload: ArrayList<Intent>,
    ): Intent
}