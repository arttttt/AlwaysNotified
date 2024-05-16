package com.arttttt.appslist.impl.domain.store

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.arttttt.appslist.impl.domain.entity.ActivityInfo
import com.arttttt.appslist.impl.domain.repository.AppsRepository
import com.arttttt.appslist.SelectedActivity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.joinAll
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
            is AppsStore.Action.GetInstalledApplications -> loadApps()
        }
    }

    override fun executeIntent(intent: AppsStore.Intent) {
        when (intent) {
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
                            appsRepository.removeActivity(mutableActivities.getValue(pkg))
                            mutableActivities.remove(pkg)
                        } else {
                            mutableActivities[pkg] = selectedActivity
                            appsRepository.saveActivity(selectedActivity)
                        }

                        mutableActivities.toMap()
                    }
            }
                .let(AppsStore.Message::SelectedActivitiesChanged)
                .let(::dispatch)

            publish(AppsStore.Label.ActivitiesChanged)
        }
    }

    private fun loadApps() {
        scope.launch {
            dispatch(AppsStore.Message.ProgressStarted)

            joinAll(
                launch { getInstalledApplications() },
                launch { getSelectedActivities() },
            )

            dispatch(AppsStore.Message.ProgressFinished)
        }
    }

    private suspend fun getInstalledApplications() {
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
    }

    private suspend fun getSelectedActivities() {
        val selectedActivities = appsRepository.getSelectedApps()

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