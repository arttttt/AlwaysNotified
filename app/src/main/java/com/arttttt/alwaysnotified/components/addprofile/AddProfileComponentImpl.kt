package com.arttttt.alwaysnotified.components.addprofile

import com.arttttt.alwaysnotified.arch.shared.context.AppComponentContext
import com.arttttt.alwaysnotified.arch.shared.dialog.DismissEvent
import com.arttttt.alwaysnotified.arch.shared.dialog.DismissEventConsumer
import com.arttttt.alwaysnotified.arch.shared.dialog.DismissEventDelegate
import com.arttttt.alwaysnotified.arch.shared.dialog.DismissEventProducer
import com.arttttt.alwaysnotified.arch.shared.events.producer.EventsProducerDelegate
import com.arttttt.alwaysnotified.arch.shared.events.producer.EventsProducerDelegateImpl

class AddProfileComponentImpl(
    context: AppComponentContext,
    dismissEventDelegate: DismissEventDelegate = DismissEventDelegate()
) : AddProfileComponent,
    AppComponentContext by context,
    EventsProducerDelegate<AddProfileComponent.Event> by EventsProducerDelegateImpl(),
    DismissEventConsumer by dismissEventDelegate,
    DismissEventProducer by dismissEventDelegate {

    override fun createProfileClicked(
        title: String,
        color: Int,
        addSelectedApps: Boolean,
    ) {
        dispatch(AddProfileComponent.Event.CreateProfile(title, color, addSelectedApps))
        onDismiss(DismissEvent)
    }
}