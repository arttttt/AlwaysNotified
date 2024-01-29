package com.arttttt.alwaysnotified.data.repository

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import com.arttttt.alwaysnotified.data.database.dao.ProfilesDao
import com.arttttt.alwaysnotified.domain.entity.info.ActivityInfo
import com.arttttt.alwaysnotified.domain.entity.info.AppInfo
import com.arttttt.alwaysnotified.domain.entity.profiles.Profile
import com.arttttt.alwaysnotified.domain.entity.profiles.SelectedActivity
import com.arttttt.alwaysnotified.domain.repository.AppsRepository

class AppsRepositoryImpl(
    private val context: Context,
    private val profilesDao: ProfilesDao,
) : AppsRepository {

    override suspend fun getInstalledApplications(): List<AppInfo> {
        val pm = context.packageManager

        return pm
            .getInstalledPackages(PackageManager.PackageInfoFlags.of(0))
            .mapNotNull { info ->
                val title = pm.getApplicationLabel(info.applicationInfo)
                val activities = pm
                    .getPackageInfo(
                        info.packageName,
                        PackageManager.PackageInfoFlags.of(
                            PackageManager.GET_ACTIVITIES.toLong(),
                        )
                    )
                    .activities

                if (title.isEmpty() || info.isSystemPackage || !info.applicationInfo.enabled || activities.isNullOrEmpty() || info.packageName == context.packageName) {
                    null
                } else {
                    AppInfo(
                        title = title.toString(),
                        pkg = info.packageName,
                        isExpanded = false,
                        activities = activities
                            .mapNotNull { activityInfo ->
                                if (activityInfo.exported) {
                                    ActivityInfo(
                                        title = activityInfo.name.substringAfterLast('.'),
                                        name = activityInfo.name,
                                        pkg = info.packageName,
                                    )
                                } else {
                                    null
                                }
                            }
                            .toSet(),
                    )
                }
            }
    }

    override suspend fun getAppsForProfile(profile: Profile): List<SelectedActivity> {
        return profilesDao
            .getSelectedActivitiesForUuid(
                uuid = profile.uuid
            )
            .map { activity ->
                SelectedActivity(
                    pkg = activity.pkg,
                    activity = activity.activity,
                )
            }
    }

    private val PackageInfo.isSystemPackage: Boolean
        get() {
            return applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM != 0
        }
}