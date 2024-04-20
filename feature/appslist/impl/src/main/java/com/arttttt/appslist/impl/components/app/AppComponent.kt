package com.arttttt.appslist.impl.components.app

import android.graphics.drawable.Drawable
import com.arttttt.appslist.SelectedActivity
import com.arttttt.appslist.impl.domain.entity.AppInfo
import com.arttttt.core.arch.DecomposeComponent
import com.arttttt.core.arch.content.ComponentContentOwner
import com.arttttt.core.arch.context.AppComponentContext
import com.arttttt.core.arch.dialog.DismissEventConsumer
import com.arttttt.core.arch.dialog.DismissEventProducer
import com.arttttt.core.arch.events.producer.EventsProducer
import com.arttttt.lazylist.ListItem
import kotlinx.coroutines.flow.StateFlow

internal interface AppComponent : DecomposeComponent,
    ComponentContentOwner,
    EventsProducer<AppComponent.Event>,
    DismissEventConsumer,
    DismissEventProducer {

    fun interface Factory {

        fun create(
            context: AppComponentContext,
            app: AppInfo,
            selectedActivity: SelectedActivity?,
        ): AppComponent
    }

    sealed interface Event {

        data class EditingFinished(
            val pkg: String,
            val selectedActivity: SelectedActivity?
        ) : Event
    }

    data class State(
        val app: AppInfo,
        val selectedActivity: SelectedActivity?,
        val isDirty: Boolean,
    )

    data class UIState(
        val title: String,
        val icon: Drawable?,
        val isManualModeAvailable: Boolean,
        val manualModeEnabled: Boolean,
        val items: List<ListItem>
    )

    val uiStates: StateFlow<UIState>

    fun onActivityClicked(name: String)
    fun onManualModeChanged()
}