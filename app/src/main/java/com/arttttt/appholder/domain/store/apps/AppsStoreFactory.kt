package com.arttttt.appholder.domain.store.apps

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arttttt.appholder.domain.repository.AppsRepository
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
                bootstrapper = appsStoreBootstrapper(
                    dispatcher = dispatcher,
                ),
                executorFactory = appsStoreExecutor(
                    dispatcher = dispatcher,
                    appsRepository = appsRepository,
                ),
                reducer = appsStoreReducer(),
            ) {}
    }
}