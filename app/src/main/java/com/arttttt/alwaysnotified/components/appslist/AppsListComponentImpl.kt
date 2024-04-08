package com.arttttt.alwaysnotified.components.appslist

import com.arkivanov.decompose.childContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.update
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.states
import com.arttttt.alwaysnotified.AppsLauncher
import com.arttttt.alwaysnotified.arch.shared.context.AppComponentContext
import com.arttttt.alwaysnotified.arch.shared.context.wrapComponentContext
import com.arttttt.alwaysnotified.arch.shared.events.producer.EventsProducerDelegate
import com.arttttt.alwaysnotified.arch.shared.events.producer.EventsProducerDelegateImpl
import com.arttttt.alwaysnotified.components.profiles.ProfilesComponent
import com.arttttt.alwaysnotified.components.topbar.TopBarComponent
import com.arttttt.alwaysnotified.components.topbar.TopBarComponentImpl
import com.arttttt.alwaysnotified.domain.store.apps.AppsStore
import com.arttttt.alwaysnotified.ui.appslist.lazylist.models.ActivityListItem
import com.arttttt.alwaysnotified.ui.appslist.lazylist.models.AppListItem
import com.arttttt.alwaysnotified.ui.appslist.lazylist.models.DividerListItem
import com.arttttt.alwaysnotified.ui.appslist.lazylist.models.ProgressListItem
import com.arttttt.alwaysnotified.ui.base.ListItem
import com.arttttt.alwaysnotified.utils.extensions.koinScope
import com.arttttt.alwaysnotified.utils.resources.ResourcesProvider
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.core.component.getScopeId
import org.koin.core.qualifier.qualifier

class AppsListComponentImpl(
    componentContext: AppComponentContext,
    resourcesProvider: ResourcesProvider,
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
            ::Pair,
        )
            .map { (appsStoreState, profilesState) ->
                val apps = appsStoreState.applications?.entries?.foldIndexed(mutableListOf<ListItem>()) { index, acc, (_, app) ->
                    acc += AppListItem(
                        pkg = app.pkg,
                        title = app.title,
                        clipTop = index == 0,
                        manualMode = appsStoreState.selectedActivities?.get(app.pkg)?.manualMode == true,
                        isManualModeAvailable = appsStoreState.selectedActivities?.get(app.pkg) != null,
                        clipBottom = index == appsStoreState.applications.entries.size - 1 && (appsStoreState.selectedApps == null || !appsStoreState.selectedApps.contains(app.pkg)),
                        icon = resourcesProvider.getDrawable(app.pkg),
                    )

                    if (appsStoreState.selectedApps?.contains(app.pkg) == true) {
                        val selectedActivity = appsStoreState.getSelectedActivityForPkg(app.pkg)

                        app.activities.mapIndexedTo(acc) { activityIndex, activity ->
                            ActivityListItem(
                                pkg = activity.pkg,
                                title = activity.title,
                                name = activity.name,
                                isSelected = activity.name.contentEquals(selectedActivity?.name, true),
                                key = "${activity.pkg}_${activity.name}",
                                clipTop = false,
                                clipBottom = index == appsStoreState.applications.entries.size - 1 && activityIndex == app.activities.size - 1
                            )
                        }
                    }

                    if (index < appsStoreState.applications.size - 1) {
                        acc += DividerListItem()
                    }

                    acc
                } ?: mutableListOf()

                if (apps.isEmpty() && appsStoreState.isInProgress) {
                    apps += ProgressListItem()
                }

                AppListComponent.UiState(
                    apps = apps.toPersistentList(),
                    isStartButtonVisible = appsStoreState.selectedActivities?.isNotEmpty() ?: false,
                    isSaveProfileButtonVisible = profilesState.isCurrentProfileDirty,
                )
            }
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