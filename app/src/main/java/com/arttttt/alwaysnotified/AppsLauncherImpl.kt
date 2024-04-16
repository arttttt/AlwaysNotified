package com.arttttt.alwaysnotified

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import com.arttttt.alwaysnotified.utils.extensions.intent
import com.arttttt.appslist.api.AppsManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AppsLauncherImpl(
    private val context: Context,
    private val appsManager: AppsManager,
) : AppsLauncher {

    override fun startAppStartupService() {
        context.startService(
            context.intent<AppStartupService>()
        )
    }

    override suspend fun launchApps() {
        appsManager.ensureReady()

        val payload = getActivitiesPayload(
            applications = appsManager.applications?.values?.toList() ?: emptyList(),
            selectedActivities = appsManager.selectedActivities ?: emptyMap(),
            isAppSelected = { pkg -> appsManager.selectedActivities?.contains(pkg) == true },
        )

        if (payload.isEmpty()) return

        context.startActivity(
            context.getHolderIntent(payload)
        )
    }

    override suspend fun getLaunchIntent(): Intent? {
        appsManager.ensureReady()

        val payload = getActivitiesPayload(
            applications = appsManager.applications?.values?.toList() ?: emptyList(),
            selectedActivities = appsManager.selectedActivities ?: emptyMap(),
            isAppSelected = { pkg -> appsManager.selectedActivities?.contains(pkg) == true },
        )

        if (payload.isEmpty()) return null

        return context.getHolderIntent(payload)
    }

    private fun Context.getHolderIntent(payload: ArrayList<Intent>): Intent {
        return intent<HolderActivity> {
            setClassName(
                packageName,
                HolderActivity::class.java.name
            )

            putExtra(
                HolderActivity.APPS_TO_START,
                payload,
            )

            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_NEW_DOCUMENT
        }
    }

    @Suppress("ReplaceGetOrSet")
    private suspend fun getActivitiesPayload(
        applications: List<AppInfo>,
        selectedActivities: Map<String, SelectedActivity>,
        isAppSelected: (String) -> Boolean,
    ): ArrayList<Intent> {
        return withContext(Dispatchers.IO) {
            val activities = applications.fold(mutableListOf<ActivityInfo>()) { acc, appInfo ->
                appInfo
                    .takeIf { isAppSelected.invoke(appInfo.pkg) }
                    ?.let {
                        appInfo
                            .activities
                            .mapNotNullTo(acc) { activityInfo ->
                                activityInfo.takeIf {
                                    selectedActivities
                                        .get(appInfo.pkg)
                                        ?.name
                                        ?.contentEquals(activityInfo.name, true)
                                        ?: false
                                }
                            }
                    }

                acc
            }

            ArrayList(
                activities.map { activityInfo ->
                    Intent().apply {
                        component = ComponentName(
                            activityInfo.pkg,
                            activityInfo.name,
                        )

                        flags = 0

                        putExtra(
                            HolderActivity.TARGET_TITLE,
                            activityInfo.name.substringAfterLast(".")
                        )
                        putExtra(
                            HolderActivity.MANUAL_MODE,
                            selectedActivities[activityInfo.pkg]?.manualMode
                        )
                    }
                }
            )
        }
    }
}