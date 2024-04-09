package com.arttttt.core.arch.events.producer

interface EventsProducerDelegate<Event : Any> : EventsProducer<Event> {

    fun dispatch(event: Event)
}