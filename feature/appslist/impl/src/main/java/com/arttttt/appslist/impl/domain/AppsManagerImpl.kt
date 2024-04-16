package com.arttttt.appslist.impl.domain

import com.arkivanov.mvikotlin.extensions.coroutines.states
import com.arttttt.alwaysnotified.AppInfo
import com.arttttt.alwaysnotified.SelectedActivity
import com.arttttt.appslist.api.AppsManager
import com.arttttt.appslist.impl.domain.store.AppsStore
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first

internal class AppsManagerImpl(
    private val appsStore: AppsStore,
) : AppsManager {

    override val applications: Map<String, AppInfo>? by appsStore.state::applications

    override val selectedActivities: Map<String, SelectedActivity>? by appsStore.state::selectedActivities

    override suspend fun ensureReady() {
        appsStore
            .states
            .filter { state -> !state.isInProgress && state.applications != null }
            .first()
    }
}