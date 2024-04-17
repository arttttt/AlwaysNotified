package com.arttttt.topbar.impl.components

import com.arkivanov.decompose.value.Value
import com.arttttt.appssearch.api.AppsSearchComponent
import com.arttttt.profiles.api.ProfilesComponent
import com.arttttt.topbar.impl.components.actions.ExpandableTopBarAction
import com.arttttt.topbar.impl.components.actions.TopBarAction
import kotlinx.coroutines.flow.Flow

internal interface InternalTopBarComponent {

    data class UiState(
        val expandedAction: ExpandableTopBarAction?,
        val actions: List<TopBarAction>,
    )

    sealed class Command {

        data class ShowMessage(val message: String) : Command()
    }

    val uiState: Value<UiState>

    val commands: Flow<Command>

    fun actionClicked(action: TopBarAction)
}