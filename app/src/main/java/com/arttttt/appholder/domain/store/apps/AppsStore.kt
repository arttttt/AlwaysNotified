package com.arttttt.appholder.domain.store.apps

import com.arkivanov.mvikotlin.core.store.Store
import com.arttttt.appholder.domain.entity.info.AppInfo
import com.arttttt.appholder.domain.entity.profiles.Profile

interface AppsStore : Store<AppsStore.Intent, AppsStore.State, AppsStore.Label> {

    data class State(
        val isInProgress: Boolean,
        val applications: Map<String, AppInfo>?,
        val selectedApps: Set<String>?,
        val selectedActivities: Map<String, String>?,
    ) {

        fun getSelectedActivityForPkg(pkg: String): String? {
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

        data object SaveApps : Intent()

        data class SelectAppsForProfile(
            val profile: Profile
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
            val selectedActivities: Map<String, String>,
        ) : Message()

        data object ProgressStarted : Message()
        data object ProgressFinished : Message()
    }

    sealed class Label {

        data object ActivitiesChanged : Label()
    }
}