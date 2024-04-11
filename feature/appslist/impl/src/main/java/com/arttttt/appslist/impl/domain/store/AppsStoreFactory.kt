package com.arttttt.appslist.impl.domain.store

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arttttt.alwaysnotified.AppsRepository
import kotlinx.coroutines.Dispatchers

class AppsStoreFactory(
    private val storeFactory: StoreFactory,
    private val appsRepository: AppsRepository,
) {

    fun create(): AppsStore {
        val dispatcher = Dispatchers.Main

        return object : AppsStore,
            Store<AppsStore.Intent, AppsStore.State, AppsStore.Label> by storeFactory.create(
                name = AppsStore::class.qualifiedName,
                initialState = AppsStore.State(
                    applications = null,
                    selectedApps = null,
                    selectedActivities = null,
                    isInProgress = false
                ),
                bootstrapper = AppsStoreBootstrapper(
                    dispatcher = dispatcher,
                ),
                executorFactory = {
                    AppsStoreExecutor(
                        dispatcher = dispatcher,
                        appsRepository = appsRepository,
                    )
                },
                reducer = AppsStoreReducer,
            ) {}
    }
}