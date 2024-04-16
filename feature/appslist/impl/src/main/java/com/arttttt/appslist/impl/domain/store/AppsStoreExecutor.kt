package com.arttttt.appslist.impl.domain.store

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.arttttt.alwaysnotified.ActivityInfo
import com.arttttt.appslist.impl.domain.repository.AppsRepository
import com.arttttt.alwaysnotified.Profile
import com.arttttt.alwaysnotified.SelectedActivity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

internal class AppsStoreExecutor(
    dispatcher: CoroutineDispatcher,
    private val appsRepository: AppsRepository,
) : CoroutineExecutor<AppsStore.Intent, AppsStore.Action, AppsStore.State, AppsStore.Message, AppsStore.Label>(
    mainContext = dispatcher
) {

    override fun executeAction(action: AppsStore.Action) {
        when (action) {
            is AppsStore.Action.GetInstalledApplications -> getInstalledApplications()
        }
    }

    override fun executeIntent(intent: AppsStore.Intent) {
        when (intent) {
            is AppsStore.Intent.SelectApp -> selectApp(intent.pkg)
            is AppsStore.Intent.SelectActivity -> selectActivity(intent.pkg, intent.name)
            is AppsStore.Intent.SelectAppsForProfile -> selectAppsForProfile(intent.profile)
            is AppsStore.Intent.ChangeManualMode -> changeManualMode(intent.pkg)
        }
    }

    private fun getInstalledApplications() {
        scope.launch {
            dispatch(AppsStore.Message.ProgressStarted)

            dispatch(
                AppsStore.Message.ApplicationsLoaded(
                    applications = withContext(Dispatchers.IO) {
                        appsRepository
                            .getInstalledApplications()
                            .map { info ->
                                info.copy(
                                    activities = info
                                        .activities
                                        .sortedBy(ActivityInfo::title)
                                        .toSet()
                                )
                            }
                            .sortedBy { info -> info.title }
                            .associateBy { info -> info.pkg }
                    },
                )
            )

            dispatch(AppsStore.Message.ProgressFinished)
        }
    }

    private fun selectApp(
        pkg: String,
    ) {
        scope.launch {
            withContext(Dispatchers.IO) {
                val selectedApps = state()
                    .selectedApps
                    ?.toMutableSet()
                    ?: mutableSetOf()

                if (!selectedApps.remove(pkg)) {
                    selectedApps.add(pkg)
                }

                selectedApps.toSet()
            }
                .let(AppsStore.Message::SelectedAppsChanged)
                .let(::dispatch)
        }
    }

    private fun selectActivity(
        pkg: String,
        name: String,
    ) {
        scope.launch {
            withContext(Dispatchers.IO) {
                state()
                    .selectedActivities
                    .let { activities ->
                        val mutableActivities = activities?.toMutableMap() ?: mutableMapOf()

                        val app = activities?.get(pkg)

                        if (app?.name?.contentEquals(name, true) == true) {
                            mutableActivities.remove(pkg)
                        } else {
                            mutableActivities[pkg] = SelectedActivity(
                                pkg = pkg,
                                name = name,
                                manualMode = false,
                            )
                        }

                        mutableActivities.toMap()
                    }
            }
                .let(AppsStore.Message::SelectedActivitiesChanged)
                .let(::dispatch)

            publish(AppsStore.Label.ActivitiesChanged)
        }
    }

    private fun selectAppsForProfile(profile: Profile) {
        scope.launch {
            val selectedActivities = appsRepository.getAppsForProfile(
                profile = profile,
            )

            selectedActivities
                .associateBy { activity ->
                    activity.pkg
                }
                .mapValues { (pkg, value) ->
                    SelectedActivity(
                        pkg = pkg,
                        name = value.name,
                        manualMode = value.manualMode,
                    )
                }
                .let(AppsStore.Message::SelectedActivitiesChanged)
                .let(::dispatch)
        }
    }

    private fun changeManualMode(pkg: String) {
        val selectedActivities = state().selectedActivities ?: return
        val selectedActivity = selectedActivities[pkg] ?: return

        scope.launch {
            dispatch(
                AppsStore.Message.SelectedActivitiesChanged(
                    selectedActivities = withContext(Dispatchers.IO) {
                        selectedActivities
                            .toMutableMap()
                            .apply {
                                put(
                                    pkg,
                                    selectedActivity.copy(
                                        manualMode = !selectedActivity.manualMode,
                                    )
                                )
                            }
                            .toMap()
                    }
                )
            )

            publish(AppsStore.Label.ActivitiesChanged)
        }
    }
}