package com.arttttt.appholder.domain.store.apps

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineBootstrapper
import kotlinx.coroutines.CoroutineDispatcher

class AppsStoreBootstrapper(
    dispatcher: CoroutineDispatcher,
) : CoroutineBootstrapper<AppsStore.Action>(dispatcher) {

    override fun invoke() {
        dispatch(AppsStore.Action.GetInstalledApplications)
    }
}