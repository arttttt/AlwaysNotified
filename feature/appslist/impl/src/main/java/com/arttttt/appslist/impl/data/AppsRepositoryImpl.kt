package com.arttttt.appslist.impl.data

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import com.arttttt.appslist.impl.domain.entity.AppInfo
import com.arttttt.appslist.impl.domain.repository.AppsRepository
import com.arttttt.database.dao.ProfilesDao

internal class AppsRepositoryImpl(
    private val context: Context,
    private val profilesDao: ProfilesDao,
) : AppsRepository {

    @SuppressLint("QueryPermissionsNeeded")
    override suspend fun getInstalledApplications(): List<AppInfo> {
        val pm = context.packageManager

        return pm
            .getInstalledPackages(
                PackageManager.PackageInfoFlags.of(
                    PackageManager.GET_SERVICES.toLong() or
                            PackageManager.GET_PROVIDERS.toLong() or
                            PackageManager.GET_RECEIVERS.toLong(),
                )
            )
            .mapNotNull { info ->
                val title = pm.getApplicationLabel(info.applicationInfo)

                if (title.isEmpty() || info.isSystemPackage || !info.applicationInfo.enabled || info.packageName == context.packageName) {
                    null
                } else {
                    AppInfo(
                        title = title.toString(),
                        pkg = info.packageName,
                        components = info.getComponents(),
                    )
                }
            }
    }

    private fun PackageInfo.getComponents(): List<AppInfo.Component> {
        return buildList {
            this@getComponents
                .services
                ?.mapTo(this) { serviceInfo ->
                    AppInfo.Component.Service(
                        title = serviceInfo.name.substringAfterLast("."),
                        name = serviceInfo.name,
                        pkg = serviceInfo.packageName,
                    )
                }

            this@getComponents
                .receivers
                ?.mapTo(this) { receiverInfo ->
                    AppInfo.Component.BroadcastReceiver(
                        title = receiverInfo.name.substringAfterLast("."),
                        name = receiverInfo.name,
                        pkg = receiverInfo.packageName,
                    )
                }

            this@getComponents
                .providers
                ?.mapTo(this) { providerInfo ->
                    AppInfo.Component.ContentProvider(
                        title = providerInfo.name.substringAfterLast("."),
                        name = providerInfo.name,
                        pkg = providerInfo.packageName,
                    )
                }
        }
    }

    private val PackageInfo.isSystemPackage: Boolean
        get() {
            return applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM != 0
        }
}