package com.arttttt.appholder.domain.store

fun appsStoreReducer(): AppsStore.State.(AppsStore.Message) -> AppsStore.State = { msg ->
    when (msg) {
        is AppsStore.Message.ApplicationsLoaded -> copy(
            applications = msg.applications,
        )
        is AppsStore.Message.SelectedAppsChanged -> copy(
            selectedApps = msg.selectedApps
        )
        is AppsStore.Message.SelectedActivitiesChanged -> copy(
            selectedActivities = msg.selectedActivities
        )
    }
}