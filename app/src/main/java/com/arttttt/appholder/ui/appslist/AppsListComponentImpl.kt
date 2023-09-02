package com.arttttt.appholder.ui.appslist

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.update
import com.arkivanov.essenty.lifecycle.doOnDestroy
import com.arkivanov.essenty.lifecycle.doOnStop
import com.arkivanov.mvikotlin.extensions.coroutines.states
import com.arttttt.appholder.AppsLauncher
import com.arttttt.appholder.ui.appslist.lazylist.models.ActivityListItem
import com.arttttt.appholder.ui.appslist.lazylist.models.AppListItem
import com.arttttt.appholder.ui.appslist.lazylist.models.DividerListItem
import com.arttttt.appholder.domain.store.AppsStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class AppsListComponentImpl(
    componentContext: ComponentContext,
    private val appsLauncher: AppsLauncher,
) : AppListComponent,
    ComponentContext by componentContext,
    KoinComponent {

    private val appsStore: AppsStore by inject()

    private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    override val state = MutableValue(
        initialValue = AppListComponent.State(
            apps = emptyList(),
        )
    )

    init {
        lifecycle.doOnDestroy {
            scope.coroutineContext.cancelChildren()
        }

        lifecycle.doOnStop {
            appsStore.accept(AppsStore.Intent.SaveApps)
        }

        appsStore
            .states
            .map { state ->
                AppListComponent.State(
                    apps = state.applications?.entries?.foldIndexed(mutableListOf()) { index, acc, (_, app) ->
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
                    } ?: emptyList()
                )
            }
            .onEach { updatedState ->
                state.update {
                    updatedState
                }
            }
            .launchIn(scope)
    }

    override fun startApps() {
        appsLauncher.startAppHolderService()
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
}