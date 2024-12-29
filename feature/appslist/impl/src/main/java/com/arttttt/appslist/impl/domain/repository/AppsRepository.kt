package com.arttttt.appslist.impl.domain.repository

import com.arttttt.appslist.impl.domain.entity.AppInfo

internal interface AppsRepository {

    suspend fun getInstalledApplications(): List<AppInfo>

    suspend fun getAllApps(): List<String>
    suspend fun saveApp(app: String)
    suspend fun removeApp(appInfo: String)
}