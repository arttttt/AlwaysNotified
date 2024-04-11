package com.arttttt.alwaysnotified

import android.content.Intent

interface AppsLauncher {

    fun startAppStartupService()

    suspend fun launchApps()

    suspend fun getLaunchIntent(): Intent?
}