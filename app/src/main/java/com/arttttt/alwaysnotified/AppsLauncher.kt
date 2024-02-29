package com.arttttt.alwaysnotified

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import com.arkivanov.mvikotlin.extensions.coroutines.states
import com.arttttt.alwaysnotified.domain.entity.info.ActivityInfo
import com.arttttt.alwaysnotified.domain.entity.info.AppInfo
import com.arttttt.alwaysnotified.domain.entity.profiles.SelectedActivity
import com.arttttt.alwaysnotified.domain.store.apps.AppsStore
import com.arttttt.alwaysnotified.utils.extensions.intent
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
            isAppSelected = { pkg -> appsStore.state.selectedActivities?.contains(pkg) == true },
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
            isAppSelected = { pkg -> appsStore.state.selectedActivities?.contains(pkg) == true },
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

                        putExtra(HolderActivity.TARGET_TITLE, activityInfo.name.substringAfterLast("."))
                        putExtra(HolderActivity.MANUAL_MODE, selectedActivities[activityInfo.pkg]?.manualMode)
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