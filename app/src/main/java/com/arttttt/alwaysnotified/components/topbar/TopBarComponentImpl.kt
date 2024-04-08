package com.arttttt.alwaysnotified.components.topbar

import com.arkivanov.decompose.childContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.update
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import com.arttttt.alwaysnotified.arch.shared.context.AppComponentContext
import com.arttttt.alwaysnotified.arch.shared.context.wrapComponentContext
import com.arttttt.alwaysnotified.components.appssearch.AppsSearchComponentImpl
import com.arttttt.alwaysnotified.components.profiles.ProfilesComponentImpl
import com.arttttt.alwaysnotified.components.topbar.actions.ExpandableTopBarAction
import com.arttttt.alwaysnotified.components.topbar.actions.TopBarAction
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.flow.MutableSharedFlow

class TopBarComponentImpl(
    componentContext: AppComponentContext,
) : TopBarComponent,
    AppComponentContext by componentContext {

    private val coroutineScope = coroutineScope()

    override val profilesComponent = ProfilesComponentImpl(
        context = wrapComponentContext(
            context = childContext(
                key = "profiles",
            ),
            parentScopeID = parentScopeID,
        )
    )

    override val appsSearchComponent = AppsSearchComponentImpl(
        context = wrapComponentContext(
            context = childContext(
                key = "apps_search",
            ),
            parentScopeID = parentScopeID,
        )
    )

    override val uiState = MutableValue(
        initialValue = TopBarComponent.UiState(
            expandedAction = null,
            actions = listOf(
                TopBarAction.Profiles(
                    component = profilesComponent
                ),
                TopBarAction.AppsSearch(
                    component = appsSearchComponent,
                ),
                TopBarAction.Settings,
            ),
        )
    )

    override val commands = MutableSharedFlow<TopBarComponent.Command>(extraBufferCapacity = 1)

    override fun actionClicked(action: TopBarAction) {
        when {
            action is ExpandableTopBarAction -> toggleExpand(action)
            action is TopBarAction.Settings -> {
                commands.tryEmit(TopBarComponent.Command.ShowMessage("Not implemented yet"))
            }
            else -> { /** TODO */ }
        }
    }

    private fun toggleExpand(action: ExpandableTopBarAction) {
        uiState.update { state ->
            state.copy(
                expandedAction = action.takeIf { action != state.expandedAction }
            )
        }
    }
}