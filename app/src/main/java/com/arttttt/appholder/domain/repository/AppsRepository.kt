package com.arttttt.appholder.domain.repository

import com.arttttt.appholder.domain.entity.info.ActivityInfo
import com.arttttt.appholder.domain.entity.info.AppInfo
import com.arttttt.appholder.domain.entity.profiles.Profile
import com.arttttt.appholder.domain.entity.profiles.SelectedActivity

interface AppsRepository {

    suspend fun getInstalledApplications(): List<AppInfo>

    suspend fun getAppsForProfile(profile: Profile): List<SelectedActivity>
}