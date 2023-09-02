package com.arttttt.appholder

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import com.arkivanov.mvikotlin.extensions.coroutines.states
import com.arttttt.appholder.domain.entity.info.ActivityInfo
import com.arttttt.appholder.domain.entity.info.AppInfo
import com.arttttt.appholder.domain.store.apps.AppsStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

/**
 * todo: introduce interface
 */
class AppsLauncher(
    private val context: Context,
    private val appsStore: AppsStore,
) {

    fun startAppStartupService() {
        context.startService(
            context.intent<AppStartupService>()
        )
    }

    suspend fun launchApps() {
        ensureReady()

        val payload = getActivitiesPayload(
            applications = appsStore.state.applications?.values?.toList() ?: emptyList(),
            selectedActivities = appsStore.state.selectedActivities ?: emptyMap(),
            isAppSelected = { pkg -> appsStore.state.selectedApps?.contains(pkg) == true },
        )

        if (payload.isEmpty()) return

        context.startActivity(
            context.getHolderIntent(payload)
        )
    }

    suspend fun getLaunchIntent(): Intent? {
        ensureReady()

        val payload = getActivitiesPayload(
            applications = appsStore.state.applications?.values?.toList() ?: emptyList(),
            selectedActivities = appsStore.state.selectedActivities ?: emptyMap(),
            isAppSelected = { pkg -> appsStore.state.selectedApps?.contains(pkg) == true },
        )

        if (payload.isEmpty()) return null

        return context.getHolderIntent(payload)
    }

    private fun Context.getHolderIntent(payload: ArrayList<Intent>): Intent {
        return activityIntent<HolderActivity> {
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

    private suspend fun getActivitiesPayload(
        applications: List<AppInfo>,
        selectedActivities: Map<String, Set<String>>,
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
                                        ?.contains(activityInfo.name)
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
                            activityInfo.name
                        )

                        flags = 0
                    }
                }
            )
        }
    }

    private suspend fun ensureReady() {
        appsStore
            .states
            .filter { state -> !state.isInProgress && state.applications != null }
            .first()
    }
}