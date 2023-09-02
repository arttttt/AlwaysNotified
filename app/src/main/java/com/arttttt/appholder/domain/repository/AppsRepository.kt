package com.arttttt.appholder.domain.repository

import com.arttttt.appholder.domain.entity.ActivityInfo
import com.arttttt.appholder.domain.entity.AppInfo

interface AppsRepository {

    suspend fun getInstalledApplications(): List<AppInfo>

    /**
     * todo: save list of [AppInfo]
     */
    suspend fun saveSelectedActivities(activities: List<ActivityInfo>)
    suspend fun getSelectedApplications(): Map<String, Set<String>>
}