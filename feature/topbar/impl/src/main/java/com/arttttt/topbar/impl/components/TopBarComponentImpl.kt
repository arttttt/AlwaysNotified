package com.arttttt.topbar.impl.components

import com.arkivanov.decompose.childContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.update
import com.arttttt.topbar.impl.components.actions.ExpandableTopBarAction
import com.arttttt.topbar.impl.components.actions.TopBarAction
import com.arttttt.appssearch.api.AppsSearchComponent
import com.arttttt.core.arch.content.ComponentContent
import com.arttttt.core.arch.context.AppComponentContext
import com.arttttt.core.arch.context.wrapComponentContext
import com.arttttt.core.arch.koinScope
import com.arttttt.profiles.api.ProfilesComponent
import com.arttttt.topbar.api.TopBarComponent
import com.arttttt.topbar.impl.ui.TopBarContent
import kotlinx.coroutines.flow.MutableSharedFlow
import org.koin.core.component.getScopeId
import org.koin.core.qualifier.qualifier

internal class TopBarComponentImpl(
    context: AppComponentContext,
) : TopBarComponent,
    InternalTopBarComponent,
    AppComponentContext by context {

    override val content: ComponentContent = TopBarContent(this)

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
        initialValue = InternalTopBarComponent.UiState(
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

    override val commands = MutableSharedFlow<InternalTopBarComponent.Command>(extraBufferCapacity = 1)

    override fun actionClicked(action: TopBarAction) {
        when {
            action is ExpandableTopBarAction -> toggleExpand(action)
            action is TopBarAction.Settings -> {
                commands.tryEmit(InternalTopBarComponent.Command.ShowMessage("Not implemented yet"))
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