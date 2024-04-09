package com.arttttt.alwaysnotified.components.appslist

import com.arkivanov.decompose.childContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.update
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.states
import com.arttttt.alwaysnotified.AppsLauncher
import com.arttttt.profiles.api.ProfilesComponent
import com.arttttt.alwaysnotified.components.topbar.TopBarComponent
import com.arttttt.alwaysnotified.components.topbar.TopBarComponentImpl
import com.arttttt.alwaysnotified.domain.store.apps.AppsStore
import com.arttttt.core.arch.koinScope
import com.arttttt.core.arch.context.AppComponentContext
import com.arttttt.core.arch.context.wrapComponentContext
import com.arttttt.core.arch.events.producer.EventsProducerDelegate
import com.arttttt.core.arch.events.producer.EventsProducerDelegateImpl
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.core.component.getScopeId
import org.koin.core.qualifier.qualifier

class AppsListComponentImpl(
    componentContext: AppComponentContext,
    transformer: AppsListTransformer,
) : AppListComponent,
    AppComponentContext by componentContext,
    EventsProducerDelegate<AppListComponent.Event> by EventsProducerDelegateImpl() {

    private val scope = koinScope(
        scopeID = getScopeId(),
        qualifier = qualifier<AppListComponent>()
    )

    private val appsStore: AppsStore by scope.inject()
    private val appsLauncher: AppsLauncher by scope.inject()

    private val coroutineScope = coroutineScope()

    override val uiState = MutableValue(
        initialValue = AppListComponent.UiState(
            apps = persistentListOf(),
            isStartButtonVisible = false,
            isSaveProfileButtonVisible = false,
        )
    )

    override val topBarComponent: TopBarComponent = TopBarComponentImpl(
        componentContext = wrapComponentContext(
            context = childContext(
                key = "topbar",
            ),
            parentScopeID = scope.id,
        ),
    )

    init {
        combine(
            appsStore.states,
            topBarComponent.profilesComponent.states,
            topBarComponent.appsSearchComponent.states,
            ::Triple,
        )
            .map(transformer::invoke)
            .flowOn(Dispatchers.IO)
            .onEach { updatedState ->
                uiState.update {
                    updatedState
                }
            }
            .launchIn(coroutineScope)

        topBarComponent
            .profilesComponent
            .states
            .map { state -> state.currentProfile }
            .filterNotNull()
            .distinctUntilChanged()
            .map(AppsStore.Intent::SelectAppsForProfile)
            .onEach(appsStore::accept)
            .launchIn(coroutineScope)

        appsStore
            .labels
            .filterIsInstance<AppsStore.Label.ActivitiesChanged>()
            .map {
                ProfilesComponent.Events.Input.MarkCurrentProfileAsDirty
            }
            .onEach(topBarComponent.profilesComponent::consume)
            .launchIn(coroutineScope)
    }

    override fun startApps() {
        coroutineScope.launch {
            appsLauncher.launchApps()
        }
    }

    override fun onAppClicked(pkg: String) {
        appsStore.accept(
            AppsStore.Intent.SelectApp(
                pkg = pkg,
            )
        )
    }

    override fun onActivityClicked(pkg: String, name: String) {
        appsStore.accept(
            AppsStore.Intent.SelectActivity(
                pkg = pkg,
                name = name,
            )
        )
    }

    override fun openSettings() {
        dispatch(AppListComponent.Event.OpenSettings)
    }

    override fun updateProfile() {
        topBarComponent.profilesComponent.consume(ProfilesComponent.Events.Input.UpdateCurrentProfile)
    }

    override fun onManualModeChanged(pkg: String) {
        appsStore.accept(
            AppsStore.Intent.ChangeManualMode(
                pkg = pkg,
            )
        )
    }
}