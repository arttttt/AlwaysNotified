package com.arttttt.appholder

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import com.arttttt.appholder.domain.entity.ActivityInfo
import com.arttttt.appholder.domain.store.AppsStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * todo: introduce interface
 */
class AppsLauncher(
    private val context: Context,
    private val appsStore: AppsStore,
) {

    fun startAppHolderService() {
        context.startForegroundService(
            context.intent<HolderService>()
        )
    }

    suspend fun launchApps() {
        val payload = withContext(Dispatchers.IO) {
            val activities = appsStore
                .state
                .applications
                ?.values
                ?.fold(mutableListOf<ActivityInfo>()) { acc, appInfo ->
                    appInfo
                        .takeIf { appsStore.state.selectedApps?.contains(appInfo.pkg) == true }
                        ?.let {
                            appInfo
                                .activities
                                .mapNotNullTo(acc) { activityInfo ->
                                    activityInfo.takeIf {
                                        appsStore
                                            .state
                                            .selectedActivities
                                            ?.get(appInfo.pkg)
                                            ?.contains(activityInfo.name)
                                            ?: false
                                    }
                                }
                        }

                    acc
                } ?: emptySet()

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

        if (payload.isEmpty()) return

        context.startActivity(
            Intent(context.applicationContext, HolderActivity::class.java).apply {
                setClassName(
                    context.packageName,
                    HolderActivity::class.java.name
                )

                putExtra(
                    HolderActivity.APPS_TO_START,
                    payload,
                )

                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_NEW_DOCUMENT or Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS
            }
        )
    }
}