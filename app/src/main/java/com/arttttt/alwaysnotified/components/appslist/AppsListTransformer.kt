package com.arttttt.alwaysnotified.components.appslist

import com.arttttt.alwaysnotified.components.appssearch.AppsSearchComponent
import com.arttttt.profiles.api.ProfilesComponent
import com.arttttt.alwaysnotified.domain.entity.info.ActivityInfo
import com.arttttt.alwaysnotified.domain.entity.info.AppInfo
import com.arttttt.profiles.api.SelectedActivity
import com.arttttt.alwaysnotified.domain.store.apps.AppsStore
import com.arttttt.alwaysnotified.ui.appslist.lazylist.models.ActivityListItem
import com.arttttt.alwaysnotified.ui.appslist.lazylist.models.AppListItem
import com.arttttt.alwaysnotified.ui.appslist.lazylist.models.DividerListItem
import com.arttttt.alwaysnotified.ui.appslist.lazylist.models.ProgressListItem
import com.arttttt.lazylist.ListItem
import com.arttttt.alwaysnotified.utils.resources.ResourcesProvider
import com.arttttt.core.arch.Transformer
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
        if (appsStoreState.needShowProgress) return listOf(ProgressListItem)

        val filteredApps = appsStoreState
            .applications!!
            .filter { (_, app) -> appsSearchState.needShowApp(app) }

        return filteredApps
            .entries
            .foldIndexed(
                initial = mutableListOf()
            ) { index, acc, (_, app) ->
                acc += app.toListItem(
                    clipTop = index == 0,
                    manualMode = appsStoreState.isManualModeForApp(app),
                    isManualModeAvailable = appsStoreState.isManualModeForAppAvailable(app),
                    clipBottom = appsStoreState.clipBottom(
                        app = app,
                        index = index,
                        filteredApps = filteredApps,
                    ),
                )

                if (appsStoreState.selectedApps?.contains(app.pkg) == true) {
                    val selectedActivity = appsStoreState.getSelectedActivityForPkg(app.pkg)

                    app.activities.mapIndexedTo(acc) { activityIndex, activity ->
                        activity.toListItem(
                            isSelected = activity.isSelected(selectedActivity),
                            clipBottom = index == filteredApps.entries.size - 1 && activityIndex == app.activities.size - 1,
                        )
                    }
                }

                if (index < filteredApps.size - 1) {
                    acc += DividerListItem()
                }

                acc
            }
    }

    private fun ActivityInfo.isSelected(selectedActivity: SelectedActivity?): Boolean {
        return name.contentEquals(selectedActivity?.name, true)
    }

    private fun AppsStore.State.getSelectedActivityForPkg(pkg: String): SelectedActivity? {
        return selectedActivities?.get(pkg)
    }

    private fun AppsStore.State.clipBottom(
        app: AppInfo,
        index: Int,
        filteredApps: Map<String, AppInfo>,
    ): Boolean {
        return index == filteredApps.entries.size - 1 && (selectedApps == null || !selectedApps.contains(app.pkg))
    }

    private fun AppsStore.State.isManualModeForAppAvailable(app: AppInfo): Boolean {
        return selectedActivities?.get(app.pkg) != null
    }

    private fun AppsStore.State.isManualModeForApp(app: AppInfo): Boolean {
        return selectedActivities?.get(app.pkg)?.manualMode == true
    }

    private fun AppsSearchComponent.State.needShowApp(app: AppInfo) : Boolean {
        return filter
            .takeIf { filter -> filter.isNotEmpty() }
            ?.let { filter ->
                app.title.startsWith(filter, true)
            }
            ?: true
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
            return isInProgress || isAppsEmpty
        }

    private val AppsStore.State.isAppsEmpty: Boolean
        get() {
            return applications?.isEmpty() ?: true
        }
}