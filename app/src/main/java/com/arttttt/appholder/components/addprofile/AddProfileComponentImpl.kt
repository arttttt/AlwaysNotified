package com.arttttt.appholder.components.addprofile

import com.arttttt.appholder.arch.shared.context.AppComponentContext
import com.arttttt.appholder.arch.shared.dialog.DismissEvent
import com.arttttt.appholder.arch.shared.dialog.DismissEventConsumer
import com.arttttt.appholder.arch.shared.dialog.DismissEventDelegate
import com.arttttt.appholder.arch.shared.dialog.DismissEventProducer
import com.arttttt.appholder.arch.shared.events.producer.EventsProducerDelegate
import com.arttttt.appholder.arch.shared.events.producer.EventsProducerDelegateImpl

class AddProfileComponentImpl(
    context: AppComponentContext,
    dismissEventDelegate: DismissEventDelegate = DismissEventDelegate()
) : AddProfileComponent,
    AppComponentContext by context,
    EventsProducerDelegate<AddProfileComponent.Event> by EventsProducerDelegateImpl(),
    DismissEventConsumer by dismissEventDelegate,
    DismissEventProducer by dismissEventDelegate {

    override fun createProfileClicked(title: String, color: Int) {
        dispatch(AddProfileComponent.Event.CreateProfile(title, color))
        onDismiss(DismissEvent)
    }
}