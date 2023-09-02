package com.arttttt.appholder.domain.store.apps

import com.arkivanov.mvikotlin.core.store.Store
import com.arttttt.appholder.domain.entity.info.AppInfo

interface AppsStore : Store<AppsStore.Intent, AppsStore.State, AppsStore.Label> {

    data class State(
        val isInProgress: Boolean,
        val applications: Map<String, AppInfo>?,
        val selectedApps: Set<String>?,
        val selectedActivities: Map<String, Set<String>>?,
    ) {

        fun getSelectedActivitiesForPkg(pkg: String): Set<String> {
            return selectedActivities?.get(pkg) ?: emptySet()
        }
    }

    sealed class Action {

        data object GetInstalledApplications : Action()
    }

    sealed class Intent {

        data class SelectApp(
            val pkg: String
        ) : Intent()

        data class SelectActivity(
            val pkg: String,
            val name: String,
        ) : Intent()

        data object PrepareAppsForLaunch : Intent()
        data object SaveApps : Intent()
    }

    sealed class Message {

        data class ApplicationsLoaded(
            val applications: Map<String, AppInfo>
        ) : Message()

        data class SelectedAppsChanged(
            val selectedApps: Set<String>,
        ) : Message()

        data class SelectedActivitiesChanged(
            val selectedActivities: Map<String, Set<String>>,
        ) : Message()

        data object ProgressStarted : Message()
        data object ProgressFinished : Message()
    }

    sealed class Label
}