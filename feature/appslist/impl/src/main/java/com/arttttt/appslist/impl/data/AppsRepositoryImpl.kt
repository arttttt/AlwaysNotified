package com.arttttt.appslist.impl.data

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import com.arttttt.database.dao.ProfilesDao
import com.arttttt.appslist.impl.domain.entity.ActivityInfo
import com.arttttt.appslist.impl.domain.entity.AppInfo
import com.arttttt.profiles.api.Profile
import com.arttttt.appslist.SelectedActivity
import com.arttttt.appslist.impl.domain.repository.AppsRepository

internal class AppsRepositoryImpl(
    private val context: Context,
    private val profilesDao: ProfilesDao,
) : AppsRepository {

    override suspend fun getInstalledApplications(): List<AppInfo> {
        val pm = context.packageManager

        return pm
            .getInstalledPackages(PackageManager.PackageInfoFlags.of(0))
            .mapNotNull { info ->
                val title = pm.getApplicationLabel(info.applicationInfo)
                val activities = kotlin
                    .runCatching {
                        pm
                            .getPackageInfo(
                                info.packageName,
                                PackageManager.PackageInfoFlags.of(
                                    PackageManager.GET_ACTIVITIES.toLong(),
                                )
                            )
                            .activities
                    }
                    .getOrNull()
                    ?.takeIf { activities -> activities.isNotEmpty() }
                    ?: return@mapNotNull null

                if (title.isEmpty() || info.isSystemPackage || !info.applicationInfo.enabled || info.packageName == context.packageName) {
                    null
                } else {
                    AppInfo(
                        title = title.toString(),
                        pkg = info.packageName,
                        isExpanded = false,
                        activities = activities
                            .filter { activityInfo -> activityInfo.exported && activityInfo.enabled }
                            .map { activityInfo ->
                                ActivityInfo(
                                    title = activityInfo.name.substringAfterLast('.'),
                                    name = activityInfo.name,
                                    pkg = info.packageName,
                                )
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
                    name = activity.activity,
                    manualMode = activity.manualMode,
                )
            }
    }

    private val PackageInfo.isSystemPackage: Boolean
        get() {
            return applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM != 0
        }
}