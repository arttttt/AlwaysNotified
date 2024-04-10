package com.arttttt.alwaysnotified.components.topbar

import com.arkivanov.decompose.childContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.update
import com.arttttt.alwaysnotified.components.topbar.actions.ExpandableTopBarAction
import com.arttttt.alwaysnotified.components.topbar.actions.TopBarAction
import com.arttttt.appssearch.api.AppsSearchComponent
import com.arttttt.core.arch.context.AppComponentContext
import com.arttttt.core.arch.context.wrapComponentContext
import com.arttttt.core.arch.koinScope
import com.arttttt.profiles.api.ProfilesComponent
import kotlinx.coroutines.flow.MutableSharedFlow
import org.koin.core.component.getScopeId
import org.koin.core.qualifier.qualifier

class TopBarComponentImpl(
    componentContext: AppComponentContext,
) : TopBarComponent,
    AppComponentContext by componentContext {

    private val koinScope = koinScope(
        scopeID = getScopeId(),
        qualifier = qualifier<TopBarComponent>(),
    )

    override val profilesComponent = koinScope
        .get<ProfilesComponent.Factory>()
        .create(
            context = wrapComponentContext(
                context = childContext(
                    key = "profiles",
                ),
                parentScopeID = koinScope.id,
            ),
        )

    override val appsSearchComponent = koinScope
        .get<AppsSearchComponent.Factory>()
        .create(
            context = wrapComponentContext(
                context = childContext(
                    key = "apps_search",
                ),
                parentScopeID = koinScope.id,
            ),
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