package com.arttttt.appslist.impl.components.appslist

import com.arttttt.appslist.SelectedActivity
import com.arttttt.appslist.impl.domain.entity.ActivityInfo
import com.arttttt.appslist.impl.domain.entity.AppInfo
import com.arttttt.appslist.impl.domain.store.AppsStore
import com.arttttt.appslist.impl.ui.appslist.lazylist.models.AppListItem
import com.arttttt.appslist.impl.ui.appslist.lazylist.models.DividerListItem
import com.arttttt.appslist.impl.ui.appslist.lazylist.models.ProgressListItem
import com.arttttt.appssearch.api.AppsSearchComponent
import com.arttttt.core.arch.Transformer
import com.arttttt.lazylist.ListItem
import com.arttttt.localization.ResourcesProvider
import com.arttttt.profiles.api.ProfilesComponent
import kotlinx.collections.immutable.toPersistentList

internal class AppsListTransformer(
    private val resourcesProvider: ResourcesProvider,
) : Transformer<Triple<AppsStore.State, ProfilesComponent.State, AppsSearchComponent.State>, InternalAppsListComponent.UiState> {

    override fun invoke(states: Triple<AppsStore.State, ProfilesComponent.State, AppsSearchComponent.State>): InternalAppsListComponent.UiState {
        val (appsStoreState, profilesState, appsSearchState) = states

        return InternalAppsListComponent.UiState(
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
            .filter { (_, app) ->
                appsSearchState.needShowApp(
                    app = app,
                    selectedApps = appsStoreState.selectedActivities?.keys ?: emptySet(),
                )
            }

        return filteredApps
            .entries
            .foldIndexed(
                initial = mutableListOf()
            ) { index, acc, (_, app) ->
                acc += app.toListItem(
                    isManualModeEnabled = appsStoreState.isManualModeEnabledForApp(app),
                    selectedActivityTitle = appsStoreState.getSelectedActivityTitle(app.pkg),
                    clipTop = index == 0,
                    clipBottom = clipBottom(
                        index = index,
                        filteredApps = filteredApps,
                    ),
                )

                if (index < filteredApps.size - 1) {
                    acc += DividerListItem()
                }

                acc
            }
    }

    private fun AppsStore.State.isManualModeEnabledForApp(app: AppInfo): Boolean {
        return selectedActivities?.get(app.pkg)?.manualMode ?: false
    }

    private fun AppsStore.State.getSelectedActivityTitle(pkg: String): String? {
        val selectedActivity = selectedActivities?.get(pkg) ?: return null

        return applications
            ?.get(pkg)
            ?.activities
            ?.find { activity ->
                activity.name == selectedActivity.name
            }
            ?.title
    }

    private fun clipBottom(
        index: Int,
        filteredApps: Map<String, AppInfo>,
    ): Boolean {
        return index == filteredApps.entries.size - 1
    }

    private fun AppsSearchComponent.State.needShowApp(
        app: AppInfo,
        selectedApps: Set<String>,
    ) : Boolean {
        val isAppSelected = if (selectedAppsOnly) {
            selectedApps.contains(app.pkg)
        } else {
            true
        }

        return isAppSelected && (filter?.let { filter -> app.title.startsWith(filter, true) } ?: true)
    }

    private fun AppInfo.toListItem(
        isManualModeEnabled: Boolean,
        selectedActivityTitle: String?,
        clipTop: Boolean,
        clipBottom: Boolean,
    ): ListItem {
        return AppListItem(
            pkg = this.pkg,
            title = this.title,
            isManualModeEnabled = isManualModeEnabled,
            selectedActivityTitle = selectedActivityTitle,
            clipTop = clipTop,
            clipBottom = clipBottom,
            icon = resourcesProvider.getDrawable(this.pkg),
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