package com.arttttt.appholder.arch.shared.events.consumer

interface EventsConsumer<in Event : Any> {

    fun consume(event: Event)
}