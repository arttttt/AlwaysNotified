package com.arttttt.appholder.arch.shared.events.producer

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

open class EventsProducerDelegateImpl<Event: Any> : EventsProducerDelegate<Event> {

    private val _events: MutableSharedFlow<Event> = MutableSharedFlow(extraBufferCapacity = 1)
    override val events: Flow<Event> = _events

    override fun dispatch(event: Event) {
        _events.tryEmit(event)
    }
}
