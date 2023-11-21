package com.arttttt.appholder.arch.shared.events.producer

import kotlinx.coroutines.flow.Flow

interface EventsProducer<out Event: Any> {

    val events: Flow<Event>
}
