package com.arttttt.appholder.arch.shared.dialog

import kotlinx.coroutines.flow.Flow

interface DismissEventProducer {

    val dismissEvents: Flow<DismissEvent>
}