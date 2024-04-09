package com.arttttt.core.arch.dialog

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

class DismissEventDelegate : DismissEventConsumer, DismissEventProducer {

    private val _events: MutableSharedFlow<DismissEvent> = MutableSharedFlow(extraBufferCapacity = 1)

    override fun onDismiss(event: DismissEvent) {
        _events.tryEmit(event)
    }

    override val dismissEvents: Flow<DismissEvent> = _events
}