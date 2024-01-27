package com.arttttt.alwaysnotified.domain.store.apps

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.arttttt.alwaysnotified.domain.entity.profiles.Profile
import com.arttttt.alwaysnotified.domain.repository.AppsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AppsStoreExecutor(
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
            is AppsStore.Intent.SaveApps -> saveApps()
            is AppsStore.Intent.SelectAppsForProfile -> selectAppsForProfile(intent.profile)
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
                                    activities = info.activities.sortedBy { activityInfo ->
                                        activityInfo.title
                                    }
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

                        if (app?.contentEquals(name, true) == true) {
                            mutableActivities.remove(pkg)
                        } else {
                            mutableActivities[pkg] = name
                        }

                        mutableActivities.toMap()
                    }
            }
                .let(AppsStore.Message::SelectedActivitiesChanged)
                .let(::dispatch)

            publish(AppsStore.Label.ActivitiesChanged)
        }
    }

    private fun saveApps() {
        scope.launch(NonCancellable) {
            withContext(Dispatchers.IO) {
                val selectedActivities = state()
                    .applications
                    ?.values
                    ?.mapNotNull { appInfo ->
                        appInfo
                            .takeIf { state().selectedApps?.contains(appInfo.pkg) == true }
                            ?.activities
                            ?.filter { activityInfo ->
                                state()
                                    .selectedActivities
                                    ?.get(appInfo.pkg)
                                    ?.contains(activityInfo.name)
                                    ?:false
                            }
                    }
                    ?.flatten()
                    ?: emptyList()

                /*appsRepository.saveSelectedActivities(
                    activities = selectedActivities
                )*/
            }
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
                .mapValues { (_, value) ->
                    value.activity
                }
                .let(AppsStore.Message::SelectedActivitiesChanged)
                .let(::dispatch)
        }
    }
}