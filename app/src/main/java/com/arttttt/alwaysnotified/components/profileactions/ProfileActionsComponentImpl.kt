package com.arttttt.alwaysnotified.components.profileactions

import com.arkivanov.decompose.value.MutableValue
import com.arttttt.core.arch.context.AppComponentContext
import com.arttttt.core.arch.dialog.DismissEventConsumer
import com.arttttt.alwaysnotified.ui.profileactions.ProfileAction
import com.arttttt.core.arch.dialog.DismissEventDelegate
import com.arttttt.core.arch.dialog.DismissEventProducer
import com.arttttt.core.arch.events.producer.EventsProducerDelegate
import com.arttttt.core.arch.events.producer.EventsProducerDelegateImpl
import kotlinx.collections.immutable.persistentListOf

class ProfileActionsComponentImpl(
    private val profileUUID: String,
    context: AppComponentContext,
    dismissEventDelegate: DismissEventDelegate = DismissEventDelegate(),
) : ProfileActionsComponent,
    EventsProducerDelegate<ProfileActionsComponent.Event> by EventsProducerDelegateImpl(),
    DismissEventConsumer by dismissEventDelegate,
    DismissEventProducer by dismissEventDelegate,
    AppComponentContext by context {

    override val uiState = MutableValue(
        initialValue = ProfileActionsComponent.UiState(
            items = persistentListOf(
                ProfileAction.Rename,
                ProfileAction.Remove,
            ),
        )
    )

    override fun profileActionClicked(action: ProfileAction) {
        when (action) {
            is ProfileAction.Rename -> dispatch(ProfileActionsComponent.Event.RenameProfile(profileUUID))
            is ProfileAction.Remove -> dispatch(ProfileActionsComponent.Event.RemoveProfile(profileUUID))
        }
    }
}