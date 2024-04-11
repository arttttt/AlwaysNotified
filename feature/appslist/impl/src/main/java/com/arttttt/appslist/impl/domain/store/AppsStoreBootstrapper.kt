package com.arttttt.appslist.impl.domain.store

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import kotlinx.coroutines.CoroutineDispatcher

class AppsStoreBootstrapper(
    dispatcher: CoroutineDispatcher,
) : CoroutineBootstrapper<AppsStore.Action>(dispatcher) {

    override fun invoke() {
        dispatch(AppsStore.Action.GetInstalledApplications)
    }
}