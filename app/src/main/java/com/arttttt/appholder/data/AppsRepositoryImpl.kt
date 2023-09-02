package com.arttttt.appholder.data

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.arttttt.appholder.domain.entity.ActivityInfo
import com.arttttt.appholder.domain.entity.AppInfo
import com.arttttt.appholder.domain.repository.AppsRepository
import kotlinx.coroutines.flow.first
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class AppsRepositoryImpl(
    private val context: Context,
) : AppsRepository {

    companion object {

        private const val STARTUP_PREFERENCES = "startup_preferences"

        private val selectedAppsKey = stringPreferencesKey("selected_apps")
    }

    private val Context.dataStore by preferencesDataStore(
        name = STARTUP_PREFERENCES,
        corruptionHandler = ReplaceFileCorruptionHandler { emptyPreferences() },
        produceMigrations = { listOf() },
    )

    private val dataStore: DataStore<Preferences>
        get() = context.dataStore

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
                            },
                    )
                }
            }
    }

    override suspend fun saveSelectedActivities(activities: List<ActivityInfo>) {
        dataStore.edit { preferences ->
            preferences[selectedAppsKey] = activities
                .groupBy(
                    keySelector = ActivityInfo::pkg::get,
                    valueTransform = ActivityInfo::name::get,
                )
                .let(Json::encodeToString)
        }
    }

    override suspend fun getSelectedApplications(): Map<String, Set<String>> {
        return dataStore
            .data
            .first()
            .get(selectedAppsKey)
            ?.let(Json::decodeFromString)
            ?: emptyMap()
    }

    private val PackageInfo.isSystemPackage: Boolean
        get() {
            return applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM != 0
        }
}