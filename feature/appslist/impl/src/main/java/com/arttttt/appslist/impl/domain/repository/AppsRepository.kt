package com.arttttt.appslist.impl.domain.repository

import com.arttttt.appslist.impl.domain.entity.AppInfo

internal interface AppsRepository {

    suspend fun getInstalledApplications(): List<AppInfo>
}