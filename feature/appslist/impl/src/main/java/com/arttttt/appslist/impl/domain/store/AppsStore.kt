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
        applications = null,
        isInProgress = false
    ),
    actor = AppsStoreActor(
        appsRepository = appsRepository,
    )
) {

    sealed interface Intent

    data class State(
        val isInProgress: Boolean,
        val applications: Map<String, AppInfo>?,
    )

    sealed interface SideEffect {

        data object ActivitiesChanged : SideEffect
    }
}