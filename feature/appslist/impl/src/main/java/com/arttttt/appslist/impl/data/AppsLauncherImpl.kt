package com.arttttt.appslist.impl.data

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import com.arkivanov.mvikotlin.extensions.coroutines.states
import com.arttttt.alwaysnotified.ActivityInfo
import com.arttttt.alwaysnotified.AppInfo
import com.arttttt.appslist.impl.domain.AppsLauncher
import com.arttttt.alwaysnotified.SelectedActivity
import com.arttttt.appslist.api.AppsLauncherIntentHelper
import com.arttttt.appslist.impl.domain.store.AppsStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

internal class AppsLauncherImpl(
    private val context: Context,
    private val appsStore: AppsStore,
    private val intentHelper: AppsLauncherIntentHelper,
) : AppsLauncher {

    override suspend fun launchApps() {
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

    private suspend fun ensureReady() {
        appsStore
            .states
            .filter { state -> !state.isInProgress && state.applications != null }
            .first()
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

                        intentHelper.putExtra(
                            intent = this,
                            activityInfo = activityInfo,
                            selectedActivities = selectedActivities,
                        )
                    }
                }
            )
        }
    }

    private fun Context.getHolderIntent(payload: ArrayList<Intent>): Intent {
        return intentHelper.createHolderIntent(
            context = this,
            payload = payload,
        )
    }
}