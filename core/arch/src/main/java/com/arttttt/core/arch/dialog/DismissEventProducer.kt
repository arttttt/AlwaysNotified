package com.arttttt.core.arch.dialog

import kotlinx.coroutines.flow.Flow

interface DismissEventProducer {

    val dismissEvents: Flow<DismissEvent>
}