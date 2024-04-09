package com.arttttt.alwaysnotified.domain.repository

import com.arttttt.alwaysnotified.domain.entity.info.AppInfo
import com.arttttt.profiles.api.Profile
import com.arttttt.profiles.api.SelectedActivity

interface AppsRepository {

    suspend fun getInstalledApplications(): List<AppInfo>

    suspend fun getAppsForProfile(profile: Profile): List<SelectedActivity>
}