package com.arttttt.appholder.arch.events

interface EventsProducerDelegate<Event : Any> : EventsProducer<Event> {

    fun dispatch(event: Event)
}