package com.arttttt.alwaysnotified.components.topbar

import com.arkivanov.decompose.value.Value
import com.arttttt.appssearch.api.AppsSearchComponent
import com.arttttt.profiles.api.ProfilesComponent
import com.arttttt.alwaysnotified.components.topbar.actions.ExpandableTopBarAction
import com.arttttt.alwaysnotified.components.topbar.actions.TopBarAction
import com.arttttt.core.arch.DecomposeComponent
import kotlinx.coroutines.flow.Flow

interface TopBarComponent : DecomposeComponent {

    data class UiState(
        val expandedAction: ExpandableTopBarAction?,
        val actions: List<TopBarAction>,
    )

    sealed class Command {

        data class ShowMessage(val message: String) : Command()
    }

    val profilesComponent: ProfilesComponent

    val appsSearchComponent: AppsSearchComponent

    val uiState: Value<UiState>

    val commands: Flow<Command>

    fun actionClicked(action: TopBarAction)
}