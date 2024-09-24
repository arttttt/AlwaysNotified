package com.arttttt.appslist.impl.domain.store

import com.arttttt.appslist.SelectedActivity
import com.arttttt.appslist.impl.domain.entity.ActivityInfo
import com.arttttt.appslist.impl.domain.entity.AppInfo
import com.arttttt.appslist.impl.domain.repository.AppsRepository
import com.arttttt.simplemvi.actor.ActorScope
import com.arttttt.simplemvi.actor.dsl.DslActorScope
import com.arttttt.simplemvi.actor.dsl.actorDsl
import com.arttttt.simplemvi.store.Store
import com.arttttt.simplemvi.store.createStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

internal class AppsStore(
    private val appsRepository: AppsRepository,
) : Store<AppsStore.Intent, AppsStore.State, AppsStore.SideEffect> by createStore(
    initialState = State(
        applications = null,
        selectedActivities = null,
        isInProgress = false
    ),
    initialIntents = listOf(
        Intent.GetInstalledApplications,
    ),
    actor = actorDsl {

        onIntent<Intent.GetInstalledApplications> {
            launch {
                reduce {
                    copy(
                        isInProgress = true,
                    )
                }

                joinAll(
                    launch { getInstalledApplications(appsRepository) },
                    launch { getSelectedActivities(appsRepository) },
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

        onIntent<Intent.SetSelectedActivity> { intent ->
            setSelectedActivity(
                pkg = intent.pkg,
                selectedActivity = intent.selectedActivity,
                appsRepository = appsRepository,
            )
        }
    }
) {

    sealed interface Intent {

        data object GetInstalledApplications : Intent

        data class SetSelectedActivity(
            val pkg: String,
            val selectedActivity: SelectedActivity?,
        ) : Intent
    }

    data class State(
        val isInProgress: Boolean,
        val applications: Map<String, AppInfo>?,
        val selectedActivities: Map<String, SelectedActivity>?,
    )

    sealed interface SideEffect {

        data object ActivitiesChanged : SideEffect
    }
}

private suspend fun ActorScope<AppsStore.Intent, AppsStore.State, AppsStore.SideEffect>.getInstalledApplications(
    appsRepository: AppsRepository,
) {
    val applications = withContext(Dispatchers.IO) {
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
    }

    reduce {
        copy(
            applications = applications,
        )
    }
}

private suspend fun ActorScope<AppsStore.Intent, AppsStore.State, AppsStore.SideEffect>.getSelectedActivities(
    appsRepository: AppsRepository,
) {
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

private fun DslActorScope<AppsStore.Intent, AppsStore.State, AppsStore.SideEffect>.setSelectedActivity(
    pkg: String,
    selectedActivity: SelectedActivity?,
    appsRepository: AppsRepository,
) {
    launch {
        val selectedActivities =withContext(Dispatchers.IO) {
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