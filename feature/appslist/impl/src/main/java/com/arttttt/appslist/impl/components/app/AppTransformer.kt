package com.arttttt.appslist.impl.components.app

import com.arttttt.appslist.impl.domain.entity.AppInfo
import com.arttttt.appslist.impl.ui.app.lazylist.models.ComponentListItem
import com.arttttt.appslist.impl.ui.app.lazylist.models.SectionTitleListItem
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
            items = createItems(state),
        )
    }

    private fun createItems(
        state: AppComponent.State,
    ): List<ListItem> {
        return state
            .app
            .components
            .groupBy { component -> component::class }
            .values
            .fold(mutableListOf()) { acc, components ->
                if (components.isNotEmpty()) {
                    acc += components.first().toSectionTitleItem()
                }

                components.mapTo(acc) { component -> component.toListItem() }
            }
    }

    private fun AppInfo.Component.toSectionTitleItem(): ListItem {
        return SectionTitleListItem(
            title = when (this) {
                is AppInfo.Component.Service -> "Services"
                is AppInfo.Component.ContentProvider -> "Providers"
            },
        )
    }

    private fun AppInfo.Component.toListItem(): ListItem {
        return ComponentListItem(
            pkg = this.pkg,
            title = this.title,
            name = this.name,
        )
    }
}