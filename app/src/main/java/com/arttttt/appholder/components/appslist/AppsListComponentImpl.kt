package com.arttttt.appholder.components.appslist

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.update
import com.arkivanov.essenty.lifecycle.doOnDestroy
import com.arkivanov.essenty.lifecycle.doOnStop
import com.arkivanov.mvikotlin.extensions.coroutines.states
import com.arttttt.appholder.AppsLauncher
import com.arttttt.appholder.arch.context.AppComponentContext
import com.arttttt.appholder.arch.events.EventsProducerDelegate
import com.arttttt.appholder.arch.events.EventsProducerDelegateImpl
import com.arttttt.appholder.domain.store.apps.AppsStore
import com.arttttt.appholder.ui.appslist.lazylist.models.ActivityListItem
import com.arttttt.appholder.ui.appslist.lazylist.models.AppListItem
import com.arttttt.appholder.ui.appslist.lazylist.models.DividerListItem
import com.arttttt.appholder.ui.appslist.lazylist.models.ProgressListItem
import com.arttttt.appholder.ui.base.ListItem
import com.arttttt.appholder.utils.extensions.koinScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class AppsListComponentImpl(
    componentContext: AppComponentContext,
) : AppListComponent,
    AppComponentContext by componentContext,
    EventsProducerDelegate<AppListComponent.Event> by EventsProducerDelegateImpl() {

    private val scope = koinScope()

    private val appsStore: AppsStore by scope.inject()
    private val appsLauncher: AppsLauncher by scope.inject()

    private val coroutineScope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    override val state = MutableValue(
        initialValue = AppListComponent.State(
            apps = emptyList(),
        )
    )

    init {
        lifecycle.doOnDestroy {
            coroutineScope.coroutineContext.cancelChildren()
        }

        lifecycle.doOnStop {
            appsStore.accept(AppsStore.Intent.SaveApps)
        }

        appsStore
            .states
            .map { state ->
                val apps = state.applications?.entries?.foldIndexed(mutableListOf<ListItem>()) { index, acc, (_, app) ->
                    acc += AppListItem(
                        pkg = app.pkg,
                        title = app.title,
                    )

                    if (state.selectedApps?.contains(app.pkg) == true) {
                        val selectedActivities = state.getSelectedActivitiesForPkg(app.pkg)

                        app.activities.mapTo(acc) { activity ->
                            ActivityListItem(
                                pkg = activity.pkg,
                                title = activity.title,
                                name = activity.name,
                                isSelected = activity.name in selectedActivities,
                                key = activity.name,
                            )
                        }
                    }

                    if (index < state.applications.size - 1) {
                        acc += DividerListItem()
                    }

                    acc
                } ?: mutableListOf()

                if (apps.isEmpty() && state.isInProgress) {
                    apps += ProgressListItem()
                }

                AppListComponent.State(
                    apps = apps
                )
            }
            .onEach { updatedState ->
                state.update {
                    updatedState
                }
            }
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
}