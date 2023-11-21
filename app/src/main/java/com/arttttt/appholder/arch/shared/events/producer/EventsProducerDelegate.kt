package com.arttttt.appholder.arch.shared.events.producer

interface EventsProducerDelegate<Event : Any> : EventsProducer<Event> {

    fun dispatch(event: Event)
}