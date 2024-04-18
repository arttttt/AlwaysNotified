package com.arttttt.appslist.api

import android.content.Context
import android.content.Intent
import com.arttttt.appslist.SelectedActivity

interface AppsLauncherIntentHelper {

    fun putExtra(
        intent: Intent,
        pkg: String,
        title: String,
        selectedActivities: Map<String, SelectedActivity>,
    )

    fun createHolderIntent(
        context: Context,
        payload: ArrayList<Intent>,
    ): Intent
}