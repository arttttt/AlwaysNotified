package com.arttttt.appslist.impl.domain.store

import com.arkivanov.mvikotlin.core.store.Reducer

internal object AppsStoreReducer : Reducer<AppsStore.State, AppsStore.Message> {

    override fun AppsStore.State.reduce(msg: AppsStore.Message): AppsStore.State {
        return when (msg) {
            is AppsStore.Message.ApplicationsLoaded -> copy(
                applications = msg.applications,
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