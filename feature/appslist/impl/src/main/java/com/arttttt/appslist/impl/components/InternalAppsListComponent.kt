package com.arttttt.appslist.impl.components

import com.arkivanov.decompose.value.Value
import com.arttttt.lazylist.ListItem
import com.arttttt.topbar.api.TopBarComponent
import kotlinx.collections.immutable.ImmutableList

internal interface InternalAppsListComponent {

    data class UiState(
        val apps: ImmutableList<ListItem>,
        val isStartButtonVisible: Boolean,
        val isSaveProfileButtonVisible: Boolean,
    )

    val uiState: Value<UiState>

    val topBarComponent: TopBarComponent

    fun onAppClicked(pkg: String)

    fun onActivityClicked(pkg: String, name: String)

    fun startApps()

    fun openSettings()

    fun updateProfile()

    fun onManualModeChanged(pkg: String)
}