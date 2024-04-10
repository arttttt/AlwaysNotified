package com.arttttt.profiles.impl.components.removeprofile

import com.arttttt.core.arch.context.AppComponentContext
import com.arttttt.core.arch.dialog.DismissEvent
import com.arttttt.core.arch.dialog.DismissEventConsumer
import com.arttttt.core.arch.dialog.DismissEventDelegate
import com.arttttt.core.arch.dialog.DismissEventProducer
import com.arttttt.core.arch.events.producer.EventsProducerDelegate
import com.arttttt.core.arch.events.producer.EventsProducerDelegateImpl

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