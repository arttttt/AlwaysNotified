package com.arttttt.appslist.impl.domain

import com.arttttt.appslist.SelectedActivity

internal interface AppsLauncher {

    suspend fun launchApps()

    fun launchApp(activity: SelectedActivity)
}