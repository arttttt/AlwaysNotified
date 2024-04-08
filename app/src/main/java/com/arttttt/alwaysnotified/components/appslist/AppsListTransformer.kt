package com.arttttt.alwaysnotified.components.appslist

import com.arttttt.alwaysnotified.arch.shared.Transformer
import com.arttttt.alwaysnotified.components.appssearch.AppsSearchComponent
import com.arttttt.alwaysnotified.components.profiles.ProfilesComponent
import com.arttttt.alwaysnotified.domain.entity.info.ActivityInfo
import com.arttttt.alwaysnotified.domain.entity.info.AppInfo
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

        return AppListComponent.UiState(
            apps = createItems(appsStoreState, appsSearchState).toPersistentList(),
            isStartButtonVisible = appsStoreState.selectedActivities?.isNotEmpty() ?: false,
            isSaveProfileButtonVisible = profilesState.isCurrentProfileDirty,
        )
    }

    private fun createItems(
        appsStoreState: AppsStore.State,
        appsSearchState: AppsSearchComponent.State,
    ): List<ListItem> {
        if (appsStoreState.needShowProgress || appsStoreState.applications == null) return listOf(ProgressListItem)

        return appsStoreState
            .applications
            .filter { (_, app) ->
                appsSearchState
                    .filter
                    .takeIf { filter -> filter.isNotEmpty() }
                    ?.let { filter ->
                        app.title.startsWith(filter, true)
                    }
                    ?: true
            }
            .entries
            .foldIndexed(
                initial = mutableListOf()
            ) { index, acc, (_, app) ->
                acc += app.toListItem(
                    clipTop = index == 0,
                    manualMode = appsStoreState.selectedActivities?.get(app.pkg)?.manualMode == true,
                    isManualModeAvailable = appsStoreState.selectedActivities?.get(app.pkg) != null,
                    clipBottom = index == appsStoreState.applications.entries.size - 1 && (appsStoreState.selectedApps == null || !appsStoreState.selectedApps.contains(app.pkg)),
                )

                if (appsStoreState.selectedApps?.contains(app.pkg) == true) {
                    val selectedActivity = appsStoreState.getSelectedActivityForPkg(app.pkg)

                    app.activities.mapIndexedTo(acc) { activityIndex, activity ->
                        activity.toListItem(
                            isSelected = activity.name.contentEquals(selectedActivity?.name, true),
                            clipBottom = index == appsStoreState.applications.entries.size - 1 && activityIndex == app.activities.size - 1,
                        )
                    }
                }

                if (index < appsStoreState.applications.size - 1) {
                    acc += DividerListItem()
                }

                acc
            }
    }

    private fun AppInfo.toListItem(
        clipTop: Boolean,
        manualMode: Boolean,
        isManualModeAvailable: Boolean,
        clipBottom: Boolean,
    ): ListItem {
        return AppListItem(
            pkg = this.pkg,
            title = this.title,
            clipTop = clipTop,
            manualMode = manualMode,
            isManualModeAvailable = isManualModeAvailable,
            clipBottom = clipBottom,
            icon = resourcesProvider.getDrawable(this.pkg),
        )
    }

    private fun ActivityInfo.toListItem(
        isSelected: Boolean,
        clipBottom: Boolean,
    ): ListItem {
        return ActivityListItem(
            pkg = this.pkg,
            title = this.title,
            name = this.name,
            isSelected = isSelected,
            key = "${this.pkg}_${this.name}",
            clipTop = false,
            clipBottom = clipBottom,
        )
    }

    private val AppsStore.State.needShowProgress: Boolean
        get() {
            return isInProgress && isAppsEmpty
        }

    private val AppsStore.State.isAppsEmpty: Boolean
        get() {
            return applications?.isEmpty() ?: true
        }
}