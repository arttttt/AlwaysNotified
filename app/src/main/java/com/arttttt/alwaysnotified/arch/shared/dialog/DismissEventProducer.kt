package com.arttttt.alwaysnotified.arch.shared.dialog

import kotlinx.coroutines.flow.Flow

interface DismissEventProducer {

    val dismissEvents: Flow<DismissEvent>
}