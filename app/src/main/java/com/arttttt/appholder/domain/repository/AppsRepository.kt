package com.arttttt.appholder.domain.repository

import com.arttttt.appholder.domain.entity.info.ActivityInfo
import com.arttttt.appholder.domain.entity.info.AppInfo

interface AppsRepository {

    suspend fun getInstalledApplications(): List<AppInfo>

    /**
     * todo: save list of [AppInfo]
     */
    suspend fun saveSelectedActivities(activities: List<ActivityInfo>)
    suspend fun getSelectedApplications(): Map<String, Set<String>>
}