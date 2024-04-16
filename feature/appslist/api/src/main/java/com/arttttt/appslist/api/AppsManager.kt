package com.arttttt.appslist.api

import com.arttttt.alwaysnotified.AppInfo
import com.arttttt.alwaysnotified.SelectedActivity

interface AppsManager {

    val applications: Map<String, AppInfo>?
    val selectedActivities: Map<String, SelectedActivity>?

    suspend fun ensureReady()
}