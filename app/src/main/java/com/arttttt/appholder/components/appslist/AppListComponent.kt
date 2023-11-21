package com.arttttt.appholder.components.appslist

import com.arkivanov.decompose.value.Value
import com.arttttt.appholder.arch.shared.DecomposeComponent
import com.arttttt.appholder.arch.shared.events.producer.EventsProducer
import com.arttttt.appholder.components.topbar.TopBarComponent
import com.arttttt.appholder.ui.base.ListItem

interface AppListComponent : DecomposeComponent, EventsProducer<AppListComponent.Event> {

    data class UiState(
        val apps: List<ListItem>,
        val isStartButtonVisible: Boolean,
        val isSaveProfileButtonVisible: Boolean,
    )

    sealed class Event {
        data object OpenSettings : Event()
    }

    val uiState: Value<UiState>

    val topBarComponent: TopBarComponent

    fun onAppClicked(pkg: String)

    fun activityClicked(pkg: String, name: String)

    fun startApps()

    fun openSettings()

    fun updateProfile()
}