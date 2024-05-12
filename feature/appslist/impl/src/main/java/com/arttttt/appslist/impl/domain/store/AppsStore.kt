package com.arttttt.appslist.impl.domain.store

import com.arkivanov.mvikotlin.core.store.Store
import com.arttttt.appslist.SelectedActivity
import com.arttttt.appslist.impl.domain.entity.AppInfo

internal interface AppsStore : Store<AppsStore.Intent, AppsStore.State, AppsStore.Label> {

    data class State(
        val isInProgress: Boolean,
        val applications: Map<String, AppInfo>?,
        val selectedActivities: Map<String, SelectedActivity>?,
    )

    sealed class Action {

        data object GetInstalledApplications : Action()
        data object GetSelectedActivities : Action()
    }

    sealed class Intent {

        data class SetSelectedActivity(
            val pkg: String,
            val selectedActivity: SelectedActivity?,
        ) : Intent()

        data class ChangeManualMode(
            val pkg: String,
        ) : Intent()
    }

    sealed class Message {

        data class ApplicationsLoaded(
            val applications: Map<String, AppInfo>
        ) : Message()

        data class SelectedActivitiesChanged(
            val selectedActivities: Map<String, SelectedActivity>,
        ) : Message()

        data object ProgressStarted : Message()
        data object ProgressFinished : Message()
    }

    sealed class Label {

        data object ActivitiesChanged : Label()
    }
}