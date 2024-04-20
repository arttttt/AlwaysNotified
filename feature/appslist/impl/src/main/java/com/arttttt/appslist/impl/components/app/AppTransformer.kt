package com.arttttt.appslist.impl.components.app

import com.arttttt.appslist.impl.domain.entity.ActivityInfo
import com.arttttt.appslist.impl.ui.appslist.lazylist.models.ActivityListItem
import com.arttttt.core.arch.Transformer
import com.arttttt.lazylist.ListItem

internal class AppTransformer : Transformer<AppComponent.State, AppComponent.UIState> {

    override fun invoke(
        state: AppComponent.State,
    ): AppComponent.UIState {
        return AppComponent.UIState(
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