package com.arttttt.appslist.impl.domain.store

import com.arttttt.appslist.impl.domain.entity.AppInfo
import com.arttttt.appslist.impl.domain.repository.AppsRepository
import com.arttttt.simplemvi.store.Store
import com.arttttt.simplemvi.store.createStore
import com.arttttt.simplemvi.store.storeName

internal class AppsStore(
    private val appsRepository: AppsRepository,
) : Store<AppsStore.Intent, AppsStore.State, AppsStore.SideEffect> by createStore(
    name = storeName<AppsStore>(),
    initialState = State(
        isInProgress = false,
        applications = emptyMap(),
        selectedApps = emptySet(),
    ),
    actor = AppsStoreActor(
        appsRepository = appsRepository,
    )
) {

    sealed interface Intent {

        data class SelectApp(val pkg: String) : Intent
    }

    data class State(
        val isInProgress: Boolean,
        val applications: Map<String, AppInfo>,
        val selectedApps: Set<String>,
    )

    sealed interface SideEffect
}