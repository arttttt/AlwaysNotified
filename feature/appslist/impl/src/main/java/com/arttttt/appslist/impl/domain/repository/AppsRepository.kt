package com.arttttt.appslist.impl.domain.repository

import com.arttttt.appslist.impl.domain.entity.AppInfo
import com.arttttt.profiles.api.Profile
import com.arttttt.appslist.SelectedActivity

internal interface AppsRepository {

    suspend fun getInstalledApplications(): List<AppInfo>

    suspend fun getAppsForProfile(profile: Profile): List<SelectedActivity>
}