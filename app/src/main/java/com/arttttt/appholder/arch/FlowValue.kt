package com.arttttt.appholder.arch

import com.arkivanov.decompose.value.Value
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.util.concurrent.locks.ReentrantLock

class FlowValue<T : Any>(
    private val flow: StateFlow<T>,
) : Value<T>() {

    private var job: Job? = null

    //TODO: replace with KMP analogue
    private val lock = ReentrantLock()

    private val observers = mutableSetOf<(T) -> Unit>()

    override val value: T
        get() = flow.value

    override fun subscribe(observer: (T) -> Unit) {
        try {
            lock.lock()

            if (observer in observers) return

            observers += observer

            if (observers.size == 1 && job == null) {
                subscribe()
            }
        } finally {
            if (lock.isHeldByCurrentThread) {
                lock.unlock()
            }
        }
    }

    override fun unsubscribe(observer: (T) -> Unit) {
        try {
            lock.lock()

            if (observer !in observers) return

            observers -= observer

            if (observers.isEmpty() && job != null) {
                unsubscribe()
            }
        } finally {
            if (lock.isHeldByCurrentThread) {
                lock.unlock()
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun subscribe() {
        job = flow
            .onEach { value ->
                try {
                    lock.lock()

                    observers.forEach { observer ->
                        observer.invoke(value)
                    }
                } finally {
                    if (lock.isHeldByCurrentThread) {
                        lock.unlock()
                    }
                }
            }
            .launchIn(GlobalScope)
    }

    private fun unsubscribe() {
        job?.cancel()
        job = null
    }
}