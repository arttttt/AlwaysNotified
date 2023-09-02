package com.arttttt.appholder.domain.store

import com.arkivanov.mvikotlin.extensions.coroutines.coroutineBootstrapper

fun appsStoreBootstrapper() = coroutineBootstrapper<AppsStore.Action> {
    dispatch(AppsStore.Action.GetInstalledApplications)
}