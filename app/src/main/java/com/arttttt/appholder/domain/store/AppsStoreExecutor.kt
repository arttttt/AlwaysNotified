package com.arttttt.appholder.domain.store

import com.arkivanov.mvikotlin.extensions.coroutines.coroutineExecutorFactory
import com.arttttt.appholder.domain.repository.AppsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.async
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

fun appsStoreExecutor(
    appsRepository: AppsRepository,
) = coroutineExecutorFactory<AppsStore.Intent, AppsStore.Action, AppsStore.State, AppsStore.Message, AppsStore.Label> {

    onAction<AppsStore.Action.GetInstalledApplications> {
        launch {
            val applicationsDeferred = async(Dispatchers.IO) {
                appsRepository
                    .getInstalledApplications()
                    .map { info ->
                        info.copy(
                            activities = info.activities.sortedBy { activityInfo ->
                                activityInfo.title
                            }
                        )
                    }
                    .sortedBy { info -> info.title }
                    .associateBy { info -> info.pkg }
            }

            val savedSelectedApplicationsDeferred = async(Dispatchers.IO) {
                appsRepository.getSelectedApplications()
            }

            joinAll(
                applicationsDeferred,
                savedSelectedApplicationsDeferred,
            )

            dispatch(
                AppsStore.Message.SelectedAppsChanged(
                    selectedApps = savedSelectedApplicationsDeferred.getCompleted().keys,
                )
            )

            dispatch(
                AppsStore.Message.SelectedActivitiesChanged(
                    selectedActivities = savedSelectedApplicationsDeferred.getCompleted(),
                )
            )

            dispatch(
                AppsStore.Message.ApplicationsLoaded(
                    applications = applicationsDeferred.getCompleted(),
                )
            )
        }
    }

    onIntent<AppsStore.Intent.SelectApp> { intent ->
        launch {
            withContext(Dispatchers.IO) {
                state
                    .selectedApps
                    ?.toMutableSet()
                    ?.also { apps ->
                        if (apps.contains(intent.pkg)) {
                            apps.remove(intent.pkg)
                        } else {
                            apps.add(intent.pkg)
                        }
                    }
                    ?.toSet()
                    ?: emptySet()
            }
                .let(AppsStore.Message::SelectedAppsChanged)
                .let(::dispatch)
        }
    }

    onIntent<AppsStore.Intent.SelectActivity> { intent ->
        launch {
            withContext(Dispatchers.IO) {
                state
                    .selectedActivities
                    .let { activities ->
                        val mutableActivities = activities?.toMutableMap() ?: mutableMapOf()

                        val app = activities
                            ?.getOrElse(intent.pkg) { emptySet() }
                            ?.toMutableSet()
                            ?: mutableSetOf()

                        if (app.contains(intent.name)) {
                            app.remove(intent.name)
                        } else {
                            app.add(intent.name)
                        }

                        mutableActivities[intent.pkg] = app

                        mutableActivities.toMap()
                    }
            }
                .let(AppsStore.Message::SelectedActivitiesChanged)
                .let(::dispatch)
        }
    }

    onIntent<AppsStore.Intent.SaveApps> {
        launch(NonCancellable) {
            withContext(Dispatchers.IO) {
                val selectedActivities = state
                    .applications
                    ?.values
                    ?.mapNotNull { appInfo ->
                        appInfo
                            .takeIf { state.selectedApps?.contains(appInfo.pkg) == true }
                            ?.activities
                            ?.filter { activityInfo ->
                                state
                                    .selectedActivities
                                    ?.get(appInfo.pkg)
                                    ?.contains(activityInfo.name)
                                    ?:false
                            }
                    }
                    ?.flatten()
                    ?: emptyList()

                appsRepository.saveSelectedActivities(
                    activities = selectedActivities
                )
            }
        }
    }
}