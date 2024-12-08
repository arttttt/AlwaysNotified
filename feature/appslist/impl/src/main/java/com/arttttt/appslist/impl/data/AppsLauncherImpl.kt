package com.arttttt.appslist.impl.data

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import com.arttttt.appslist.SelectedActivity
import com.arttttt.appslist.api.AppsLauncherIntentHelper
import com.arttttt.appslist.impl.domain.AppsLauncher
import com.arttttt.appslist.impl.domain.entity.AppInfo
import com.arttttt.appslist.impl.domain.store.AppsStore
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first

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

    override fun launchApp(activity: SelectedActivity) {
        val payload = ArrayList(
            listOf(
                Intent().apply {
                    component = ComponentName(
                        activity.pkg,
                        activity.name,
                    )

                    flags = 0

                    intentHelper.putExtra(
                        intent = this,
                        title = activity.name.substringAfterLast("."),
                        pkg = activity.pkg,
                        selectedActivities = mapOf(activity.pkg to activity),
                    )
                }
            )
        )

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
        return ArrayList()

        /*return withContext(Dispatchers.IO) {
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
                            title = activityInfo.name.substringAfterLast("."),
                            pkg = activityInfo.pkg,
                            selectedActivities = selectedActivities,
                        )
                    }
                }
            )
        }*/
    }

    private fun Context.getHolderIntent(payload: ArrayList<Intent>): Intent {
        return intentHelper.createHolderIntent(
            context = this,
            payload = payload,
        )
    }
}