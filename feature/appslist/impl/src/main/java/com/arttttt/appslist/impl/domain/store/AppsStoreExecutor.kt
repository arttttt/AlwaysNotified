package com.arttttt.appslist.impl.domain.store

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.arttttt.appslist.impl.domain.entity.ActivityInfo
import com.arttttt.appslist.impl.domain.repository.AppsRepository
import com.arttttt.profiles.api.Profile
import com.arttttt.appslist.SelectedActivity
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
            is AppsStore.Intent.SelectAppsForProfile -> selectAppsForProfile(intent.profile)
            is AppsStore.Intent.ChangeManualMode -> changeManualMode(intent.pkg)
            is AppsStore.Intent.SetSelectedActivity -> setSelectedActivity(
                pkg = intent.pkg,
                selectedActivity = intent.selectedActivity,
            )
        }
    }

    private fun setSelectedActivity(
        pkg: String,
        selectedActivity: SelectedActivity?,
    ) {
        scope.launch {
            withContext(Dispatchers.IO) {
                state()
                    .selectedActivities
                    .let { activities ->
                        val mutableActivities = activities?.toMutableMap() ?: mutableMapOf()

                        if (selectedActivity == null) {
                            mutableActivities.remove(pkg)
                        } else {
                            mutableActivities[pkg] = selectedActivity
                        }

                        mutableActivities.toMap()
                    }
            }
                .let(AppsStore.Message::SelectedActivitiesChanged)
                .let(::dispatch)

            publish(AppsStore.Label.ActivitiesChanged)
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