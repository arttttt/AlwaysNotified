package com.arttttt.appslist.impl.data

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import com.arttttt.database.dao.ProfilesDao
import com.arttttt.appslist.impl.domain.entity.ActivityInfo
import com.arttttt.appslist.impl.domain.entity.AppInfo
import com.arttttt.appslist.SelectedActivity
import com.arttttt.appslist.impl.domain.repository.AppsRepository
import com.arttttt.database.model.ActivityDbModel

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

    override suspend fun getSelectedApps(): List<SelectedActivity> {
        return profilesDao
            .getSelectedActivities()
            .map { activity ->
                SelectedActivity(
                    uuid = activity.uuid,
                    pkg = activity.pkg,
                    name = activity.activity,
                    manualMode = activity.manualMode,
                )
            }
    }

    override suspend fun saveActivity(activity: SelectedActivity) {
        profilesDao.insertActivities(
            ActivityDbModel(
                uuid = activity.uuid,
                pkg = activity.pkg,
                activity = activity.name,
                manualMode = activity.manualMode,
            )
        )
    }

    override suspend fun removeActivity(activity: SelectedActivity) {
        profilesDao.removeActivity(
            uuid = activity.uuid,
        )
    }

    private val PackageInfo.isSystemPackage: Boolean
        get() {
            return applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM != 0
        }
}