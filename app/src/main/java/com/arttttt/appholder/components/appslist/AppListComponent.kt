package com.arttttt.appholder.components.appslist

import com.arkivanov.decompose.value.Value
import com.arttttt.appholder.arch.DecomposeComponent
import com.arttttt.appholder.arch.events.EventsProducer
import com.arttttt.appholder.ui.base.ListItem

interface AppListComponent : DecomposeComponent, EventsProducer<AppListComponent.Event> {

    data class State(
        val apps: List<ListItem>,
    )

    sealed class Event {
        data object OpenSettings : Event()
    }

    val state: Value<State>

    fun onAppClicked(pkg: String)

    fun activityClicked(pkg: String, name: String)

    fun startApps()

    fun openSettings()
}