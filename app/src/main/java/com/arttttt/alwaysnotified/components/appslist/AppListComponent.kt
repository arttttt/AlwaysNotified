package com.arttttt.alwaysnotified.components.appslist

import com.arkivanov.decompose.value.Value
import com.arttttt.alwaysnotified.components.topbar.TopBarComponent
import com.arttttt.alwaysnotified.ui.base.ListItem
import com.arttttt.core.arch.DecomposeComponent
import com.arttttt.core.arch.events.producer.EventsProducer
import kotlinx.collections.immutable.ImmutableList

interface AppListComponent : DecomposeComponent, EventsProducer<AppListComponent.Event> {

    data class UiState(
        val apps: ImmutableList<ListItem>,
        val isStartButtonVisible: Boolean,
        val isSaveProfileButtonVisible: Boolean,
    )

    sealed class Event {
        data object OpenSettings : Event()
    }

    val uiState: Value<UiState>

    val topBarComponent: TopBarComponent

    fun onAppClicked(pkg: String)

    fun onActivityClicked(pkg: String, name: String)

    fun startApps()

    fun openSettings()

    fun updateProfile()

    fun onManualModeChanged(pkg: String)
}