package com.arttttt.appslist.impl.domain.repository

import com.arttttt.alwaysnotified.AppInfo
import com.arttttt.alwaysnotified.Profile
import com.arttttt.alwaysnotified.SelectedActivity

internal interface AppsRepository {

    suspend fun getInstalledApplications(): List<AppInfo>

    suspend fun getAppsForProfile(profile: Profile): List<SelectedActivity>
}