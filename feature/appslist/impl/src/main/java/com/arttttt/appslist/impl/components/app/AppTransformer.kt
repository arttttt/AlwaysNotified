package com.arttttt.appslist.impl.components.app

import com.arttttt.appslist.impl.domain.entity.ActivityInfo
import com.arttttt.appslist.impl.ui.app.lazylist.models.ActivityListItem
import com.arttttt.core.arch.Transformer
import com.arttttt.lazylist.ListItem
import com.arttttt.localization.ResourcesProvider

internal class AppTransformer(
    private val resourcesProvider: ResourcesProvider,
) : Transformer<AppComponent.State, AppComponent.UIState> {

    override fun invoke(
        state: AppComponent.State,
    ): AppComponent.UIState {
        return AppComponent.UIState(
            title = state.app.title,
            icon = resourcesProvider.getDrawable(state.app.pkg),
            items = state.app.activities.map { activityInfo ->
                activityInfo.toListItem(
                    isSelected = state.selectedActivity?.name == activityInfo.name,
                )
            }
        )
    }

    private fun ActivityInfo.toListItem(
        isSelected: Boolean,
    ): ListItem {
        return ActivityListItem(
            pkg = this.pkg,
            title = this.title,
            name = this.name,
            isSelected = isSelected,
            key = "${this.pkg}_${this.name}",
        )
    }
}