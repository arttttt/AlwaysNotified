package com.arttttt.alwaysnotified.components.removeprofile

import com.arttttt.alwaysnotified.arch.shared.context.AppComponentContext
import com.arttttt.alwaysnotified.arch.shared.dialog.DismissEvent
import com.arttttt.alwaysnotified.arch.shared.dialog.DismissEventConsumer
import com.arttttt.alwaysnotified.arch.shared.dialog.DismissEventDelegate
import com.arttttt.alwaysnotified.arch.shared.dialog.DismissEventProducer
import com.arttttt.alwaysnotified.arch.shared.events.producer.EventsProducerDelegate
import com.arttttt.alwaysnotified.arch.shared.events.producer.EventsProducerDelegateImpl

class RemoveProfileComponentImpl(
    private val profileUUID: String,
    context: AppComponentContext,
    dismissEventDelegate: DismissEventDelegate = DismissEventDelegate(),
) : RemoveProfileComponent,
    AppComponentContext by context,
    EventsProducerDelegate<RemoveProfileComponent.Event> by EventsProducerDelegateImpl(),
    DismissEventConsumer by dismissEventDelegate,
    DismissEventProducer by dismissEventDelegate {

    override fun confirmedClicked() {
        dispatch(RemoveProfileComponent.Event.RemoveProfile(profileUUID))
        onDismiss(DismissEvent)
    }
}