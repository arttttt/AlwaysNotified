package com.arttttt.appslist.impl.domain.repository

import com.arttttt.appslist.impl.domain.entity.AppInfo
import com.arttttt.appslist.SelectedActivity
import com.arttttt.appslist.impl.domain.entity.AppInfo2

internal interface AppsRepository {

    suspend fun getInstalledApplications(): List<AppInfo>

    suspend fun getSelectedApps(): List<SelectedActivity>

    suspend fun saveActivity(activity: SelectedActivity)

    suspend fun removeActivity(activity: SelectedActivity)

    suspend fun getInstalledApplications2(): List<AppInfo2>
}