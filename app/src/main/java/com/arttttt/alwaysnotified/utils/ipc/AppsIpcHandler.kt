package com.arttttt.alwaysnotified.utils.ipc

import android.os.Handler
import android.os.Looper
import android.os.Message

class AppsIpcHandler(
    looper: Looper,
    private val onMessageReceived: (AppsServiceIpcMessenger.IpcMessage) -> Unit,
) : Handler(looper) {
    
    override fun handleMessage(msg: Message) {
        AppsServiceIpcMessenger
            .IpcMessageCode
            .of(msg.what)
            .toIpcMessage(msg)
            .let(onMessageReceived)
    }

    private fun AppsServiceIpcMessenger.IpcMessageCode.toIpcMessage(
        message: Message,
    ): AppsServiceIpcMessenger.IpcMessage {
        return when (this) {
            AppsServiceIpcMessenger.IpcMessageCode.REGISTER_CLIENT -> AppsServiceIpcMessenger.IpcMessage.RegisterClient(
                messenger = message.replyTo
            )
            AppsServiceIpcMessenger.IpcMessageCode.UNREGISTER_CLIENT -> AppsServiceIpcMessenger.IpcMessage.UnregisterClient
            AppsServiceIpcMessenger.IpcMessageCode.STOP_SERVICE -> AppsServiceIpcMessenger.IpcMessage.StopService
            AppsServiceIpcMessenger.IpcMessageCode.STOP_CHAIN -> AppsServiceIpcMessenger.IpcMessage.StopChain
            AppsServiceIpcMessenger.IpcMessageCode.LAUNCH_NEXT -> AppsServiceIpcMessenger.IpcMessage.LaunchNext
        }
    }

    private fun AppsServiceIpcMessenger.IpcMessageCode.Companion.of(value: Int): AppsServiceIpcMessenger.IpcMessageCode {
        return when (value) {
            AppsServiceIpcMessenger.IpcMessageCode.REGISTER_CLIENT.ordinal -> AppsServiceIpcMessenger.IpcMessageCode.REGISTER_CLIENT
            AppsServiceIpcMessenger.IpcMessageCode.UNREGISTER_CLIENT.ordinal -> AppsServiceIpcMessenger.IpcMessageCode.UNREGISTER_CLIENT
            AppsServiceIpcMessenger.IpcMessageCode.LAUNCH_NEXT.ordinal -> AppsServiceIpcMessenger.IpcMessageCode.LAUNCH_NEXT
            AppsServiceIpcMessenger.IpcMessageCode.STOP_CHAIN.ordinal -> AppsServiceIpcMessenger.IpcMessageCode.STOP_CHAIN
            AppsServiceIpcMessenger.IpcMessageCode.STOP_SERVICE.ordinal -> AppsServiceIpcMessenger.IpcMessageCode.STOP_SERVICE
            else -> throw IllegalArgumentException("unknown message code: $value")
        }
    }
}