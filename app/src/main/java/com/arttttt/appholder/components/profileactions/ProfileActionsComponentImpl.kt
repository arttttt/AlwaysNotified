package com.arttttt.appholder.components.profileactions

import com.arkivanov.decompose.value.MutableValue
import com.arttttt.appholder.arch.shared.context.AppComponentContext
import com.arttttt.appholder.arch.shared.dialog.DismissEventConsumer
import com.arttttt.appholder.arch.shared.dialog.DismissEventDelegate
import com.arttttt.appholder.arch.shared.dialog.DismissEventProducer
import com.arttttt.appholder.arch.shared.events.producer.EventsProducerDelegate
import com.arttttt.appholder.arch.shared.events.producer.EventsProducerDelegateImpl
import com.arttttt.appholder.ui.profileactions.ProfileAction
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