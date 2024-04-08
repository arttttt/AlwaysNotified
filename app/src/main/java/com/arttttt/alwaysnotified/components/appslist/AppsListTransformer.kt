package com.arttttt.alwaysnotified.components.appslist

import com.arttttt.alwaysnotified.arch.shared.Transformer
import com.arttttt.alwaysnotified.components.appssearch.AppsSearchComponent
import com.arttttt.alwaysnotified.components.profiles.ProfilesComponent
import com.arttttt.alwaysnotified.domain.store.apps.AppsStore
import com.arttttt.alwaysnotified.ui.appslist.lazylist.models.ActivityListItem
import com.arttttt.alwaysnotified.ui.appslist.lazylist.models.AppListItem
import com.arttttt.alwaysnotified.ui.appslist.lazylist.models.DividerListItem
import com.arttttt.alwaysnotified.ui.appslist.lazylist.models.ProgressListItem
import com.arttttt.alwaysnotified.ui.base.ListItem
import com.arttttt.alwaysnotified.utils.resources.ResourcesProvider
import kotlinx.collections.immutable.toPersistentList

class AppsListTransformer(
    private val resourcesProvider: ResourcesProvider,
) : Transformer<Triple<AppsStore.State, ProfilesComponent.State, AppsSearchComponent.State>, AppListComponent.UiState> {

    override fun invoke(states: Triple<AppsStore.State, ProfilesComponent.State, AppsSearchComponent.State>): AppListComponent.UiState {
        val (appsStoreState, profilesState, appsSearchState) = states

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

        return AppListComponent.UiState(
            apps = apps.toPersistentList(),
            isStartButtonVisible = appsStoreState.selectedActivities?.isNotEmpty() ?: false,
            isSaveProfileButtonVisible = profilesState.isCurrentProfileDirty,
        )
    }
}