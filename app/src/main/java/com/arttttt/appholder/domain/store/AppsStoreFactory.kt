package com.arttttt.appholder.domain.store

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arttttt.appholder.domain.repository.AppsRepository

class AppsStoreFactory(
    private val storeFactory: StoreFactory,
    private val appsRepository: AppsRepository,
) {

    fun create(): AppsStore {
        return object : AppsStore,
            Store<AppsStore.Intent, AppsStore.State, AppsStore.Label> by storeFactory.create(
            initialState = AppsStore.State(
                applications = null,
                selectedApps = null,
                selectedActivities = null,
            ),
            bootstrapper = appsStoreBootstrapper(),
            executorFactory = appsStoreExecutor(
                appsRepository = appsRepository,
            ),
            reducer = appsStoreReducer()
        ) {}
    }
}