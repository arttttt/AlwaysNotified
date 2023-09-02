package com.arttttt.appholder.domain.store.apps

import com.arkivanov.mvikotlin.extensions.coroutines.coroutineBootstrapper
import kotlinx.coroutines.CoroutineDispatcher

fun appsStoreBootstrapper(
    dispatcher: CoroutineDispatcher
) = coroutineBootstrapper<AppsStore.Action>(dispatcher) {
    dispatch(AppsStore.Action.GetInstalledApplications)
}