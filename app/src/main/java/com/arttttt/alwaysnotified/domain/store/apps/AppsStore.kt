package com.arttttt.alwaysnotified.domain.store.apps

import com.arkivanov.mvikotlin.core.store.Store
import com.arttttt.alwaysnotified.domain.entity.info.AppInfo
import com.arttttt.alwaysnotified.domain.entity.profiles.Profile
import com.arttttt.alwaysnotified.domain.entity.profiles.SelectedActivity

interface AppsStore : Store<AppsStore.Intent, AppsStore.State, AppsStore.Label> {

    data class State(
        val isInProgress: Boolean,
        val applications: Map<String, AppInfo>?,
        val selectedApps: Set<String>?,
        val selectedActivities: Map<String, SelectedActivity>?,
    ) {

        fun getSelectedActivityForPkg(pkg: String): SelectedActivity? {
            return selectedActivities?.get(pkg)
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

        data class SelectAppsForProfile(
            val profile: Profile
        ) : Intent()

        data class ChangeManualMode(
            val pkg: String,
        ) : Intent()
    }

    sealed class Message {

        data class ApplicationsLoaded(
            val applications: Map<String, AppInfo>
        ) : Message()

        data class SelectedAppsChanged(
            val selectedApps: Set<String>,
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