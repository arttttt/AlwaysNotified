package com.arttttt.core.arch.events.consumer

interface EventsConsumer<in Event : Any> {

    fun consume(event: Event)
}