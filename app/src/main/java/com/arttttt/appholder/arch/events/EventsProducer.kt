package com.arttttt.appholder.arch.events

import kotlinx.coroutines.flow.Flow

interface EventsProducer<out Event: Any> {

    val events: Flow<Event>
}
