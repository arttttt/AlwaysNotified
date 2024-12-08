package com.arttttt.appslist.impl.domain.store

import com.arttttt.appslist.SelectedActivity
import com.arttttt.appslist.impl.domain.repository.AppsRepository
import com.arttttt.simplemvi.actor.DefaultActor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

internal class AppsStoreActor(
    private val appsRepository: AppsRepository,
) : DefaultActor<AppsStore.Intent, AppsStore.State, AppsStore.SideEffect>() {

    override fun onInit() {
        scope.launch {
            reduce {
                copy(
                    isInProgress = true,
                )
            }

            joinAll(
                launch { getInstalledApplications() },
                launch { getSelectedActivities() },
            )
        }
            .invokeOnCompletion {
                reduce {
                    copy(
                        isInProgress = false,
                    )
                }
            }
    }

    override fun handleIntent(intent: AppsStore.Intent) {
        when (intent) {
            is AppsStore.Intent.SetSelectedActivity -> setSelectedActivity(
                pkg = intent.pkg,
                selectedActivity = intent.selectedActivity,
            )
        }
    }

    private suspend fun getInstalledApplications() {
        val applications = withContext(Dispatchers.IO) {
            appsRepository
                .getInstalledApplications()
                .sortedBy { info -> info.title }
                .associateBy { info -> info.pkg }
        }

        reduce {
            copy(
                applications = applications,
            )
        }
    }

    private suspend fun getSelectedActivities() {
        val selectedActivities = appsRepository
            .getSelectedApps()
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

        reduce {
            copy(
                selectedActivities = selectedActivities,
            )
        }
    }

    private fun setSelectedActivity(
        pkg: String,
        selectedActivity: SelectedActivity?,
    ) {
        scope.launch {
            val selectedActivities = withContext(Dispatchers.IO) {
                state
                    .selectedActivities
                    .let { activities ->
                        val mutableActivities = activities?.toMutableMap() ?: mutableMapOf()

                        if (selectedActivity == null) {
                            if (mutableActivities.contains(pkg)) {
                                appsRepository.removeActivity(mutableActivities.getValue(pkg))
                            }
                            mutableActivities.remove(pkg)
                        } else {
                            mutableActivities[pkg] = selectedActivity
                            appsRepository.saveActivity(selectedActivity)
                        }

                        mutableActivities.toMap()
                    }
            }

            reduce {
                copy(
                    selectedActivities = selectedActivities,
                )
            }

            sideEffect(AppsStore.SideEffect.ActivitiesChanged)
        }
    }
}