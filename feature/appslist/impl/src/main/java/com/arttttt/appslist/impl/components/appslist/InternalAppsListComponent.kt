package com.arttttt.appslist.impl.components.appslist

import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.value.Value
import com.arttttt.core.arch.DecomposeComponent
import com.arttttt.lazylist.ListItem
import com.arttttt.topbar.api.TopBarComponent
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.StateFlow

internal interface InternalAppsListComponent {

    data class UiState(
        val apps: ImmutableList<ListItem>,
        val isStartButtonVisible: Boolean,
    )

    val slot: Value<ChildSlot<*, DecomposeComponent>>

    val uiState: StateFlow<UiState>

    val topBarComponent: TopBarComponent

    fun onAppClicked(pkg: String)

    fun startApps()

    fun openSettings()
}