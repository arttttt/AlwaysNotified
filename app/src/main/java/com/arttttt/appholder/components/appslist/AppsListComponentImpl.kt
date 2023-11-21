package com.arttttt.appholder.components.appslist

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.update
import com.arkivanov.essenty.lifecycle.doOnStop
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.states
import com.arttttt.appholder.AppsLauncher
import com.arttttt.appholder.arch.shared.context.AppComponentContext
import com.arttttt.appholder.arch.shared.context.childAppContext
import com.arttttt.appholder.arch.shared.events.producer.EventsProducerDelegate
import com.arttttt.appholder.arch.shared.events.producer.EventsProducerDelegateImpl
import com.arttttt.appholder.components.topbar.TopBarComponent
import com.arttttt.appholder.components.topbar.TopBarComponentImpl
import com.arttttt.appholder.domain.store.apps.AppsStore
import com.arttttt.appholder.ui.appslist.lazylist.models.ActivityListItem
import com.arttttt.appholder.ui.appslist.lazylist.models.AppListItem
import com.arttttt.appholder.ui.appslist.lazylist.models.DividerListItem
import com.arttttt.appholder.ui.appslist.lazylist.models.ProgressListItem
import com.arttttt.appholder.ui.base.ListItem
import com.arttttt.appholder.utils.extensions.koinScope
import com.arttttt.appholder.utils.resources.ResourcesProvider
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class AppsListComponentImpl(
    componentContext: AppComponentContext,
    resourcesProvider: ResourcesProvider,
) : AppListComponent,
    AppComponentContext by componentContext,
    EventsProducerDelegate<AppListComponent.Event> by EventsProducerDelegateImpl() {

    private val scope = koinScope()

    private val appsStore: AppsStore by scope.inject()
    private val appsLauncher: AppsLauncher by scope.inject()

    override val uiState = MutableValue(
        initialValue = AppListComponent.UiState(
            apps = emptyList(),
            isStartButtonVisible = false,
            isSaveProfileButtonVisible = false,
        )
    )

    override val topBarComponent: TopBarComponent = TopBarComponentImpl(
        componentContext = childAppContext(
            key = "topbar",
        )
    )

    init {
        lifecycle.doOnStop {
            appsStore.accept(AppsStore.Intent.SaveApps)
        }

        combine(
            appsStore.states,
            topBarComponent.states,
            ::Pair,
        )
            .map { (appsStoreState, topBarState) ->
                val apps = appsStoreState.applications?.entries?.foldIndexed(mutableListOf<ListItem>()) { index, acc, (_, app) ->
                    acc += AppListItem(
                        pkg = app.pkg,
                        title = app.title,
                        clipTop = index == 0,
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
                                isSelected = activity.name.contentEquals(selectedActivity, true),
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
                    apps = apps,
                    isStartButtonVisible = appsStoreState.selectedActivities?.isNotEmpty() ?: false,
                    isSaveProfileButtonVisible = topBarState.isProfileDirty,
                )
            }
            .onEach { updatedState ->
                uiState.update {
                    updatedState
                }
            }
            .launchIn(coroutineScope)

        topBarComponent
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
                TopBarComponent.Events.Input.MarkProfileAsDirty
            }
            .onEach(topBarComponent::consume)
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

    override fun activityClicked(pkg: String, name: String) {
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
        topBarComponent.consume(TopBarComponent.Events.Input.UpdateCurrentProfile)
    }
}