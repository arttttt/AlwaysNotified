package com.arttttt.alwaysnotified.domain.store.apps

import com.arkivanov.mvikotlin.core.store.Reducer

object AppsStoreReducer : Reducer<AppsStore.State, AppsStore.Message> {

    override fun AppsStore.State.reduce(msg: AppsStore.Message): AppsStore.State {
        return when (msg) {
            is AppsStore.Message.ApplicationsLoaded -> copy(
                applications = msg.applications,
            )
            is AppsStore.Message.SelectedAppsChanged -> copy(
                selectedApps = msg.selectedApps
            )
            is AppsStore.Message.SelectedActivitiesChanged -> copy(
                selectedActivities = msg.selectedActivities
            )
            is AppsStore.Message.ProgressStarted -> copy(
                isInProgress = true
            )
            is AppsStore.Message.ProgressFinished -> copy(
                isInProgress = false,
            )
        }
    }
}