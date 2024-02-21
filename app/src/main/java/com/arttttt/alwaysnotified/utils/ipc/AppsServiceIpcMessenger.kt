package com.arttttt.alwaysnotified.utils.ipc

import android.os.IBinder
import android.os.Looper
import android.os.Message
import android.os.Messenger

/**
 * todo: simplify
 */
class AppsServiceIpcMessenger(
    val tag: String,
    private val onMessageReceived: (IpcMessage) -> Unit
) {

    enum class IpcMessageCode {
        REGISTER_CLIENT,
        UNREGISTER_CLIENT,
        STOP_SERVICE,
        LAUNCH_NEXT,
        STOP_CHAIN,
        HIDE_LAUNCH_BUTTON,
        SHOW_LAUNCH_BUTTON;

        companion object;
    }

    sealed class IpcMessage {
        abstract val code: IpcMessageCode

        data class RegisterClient(
            val messenger: Messenger,
        ) : IpcMessage() {
            override val code = IpcMessageCode.REGISTER_CLIENT
        }

        data object UnregisterClient : IpcMessage() {
            override val code = IpcMessageCode.UNREGISTER_CLIENT
        }

        data object StopService : IpcMessage() {
            override val code = IpcMessageCode.STOP_SERVICE
        }

        data object LaunchNext : IpcMessage() {
            override val code = IpcMessageCode.LAUNCH_NEXT
        }

        data object StopChain : IpcMessage() {
            override val code = IpcMessageCode.STOP_CHAIN
        }

        data object HideLaunchButton : IpcMessage() {
            override val code = IpcMessageCode.HIDE_LAUNCH_BUTTON
        }

        data object ShowLaunchButton : IpcMessage() {
            override val code: IpcMessageCode = IpcMessageCode.SHOW_LAUNCH_BUTTON
        }
    }

    private val messenger by lazy {
        Messenger(
            AppsIpcHandler(
                looper = Looper.myLooper()!!,
                onMessageReceived = ::onMessageReceived,
            )
        )
    }

    private var externalMessenger: Messenger? = null

    val binder: IBinder
        get() = messenger.binder

    fun sendMessage(message: IpcMessage) {
        when (message) {
            is IpcMessage.RegisterClient -> {
                message.messenger.send(message.toSystemMessage())
                externalMessenger = message.messenger
            }
            is IpcMessage.UnregisterClient -> {
                externalMessenger?.send(message.toSystemMessage())
                externalMessenger = null
            }
            is IpcMessage.LaunchNext -> {
                externalMessenger?.send(message.toSystemMessage())
            }
            is IpcMessage.StopService -> {
                externalMessenger?.send(message.toSystemMessage())
            }
            is IpcMessage.StopChain -> {
                externalMessenger?.send(message.toSystemMessage())
            }
            is IpcMessage.HideLaunchButton -> {
                externalMessenger?.send(message.toSystemMessage())
            }
            is IpcMessage.ShowLaunchButton -> {
                externalMessenger?.send(message.toSystemMessage())
            }
        }
    }

    private fun onMessageReceived(message: IpcMessage) {
        when (message) {
            is IpcMessage.RegisterClient -> {
                externalMessenger = message.messenger
            }
            is IpcMessage.UnregisterClient -> {
                externalMessenger = null
            }
            is IpcMessage.StopChain -> {
                onMessageReceived.invoke(message)
            }
            is IpcMessage.StopService -> {
                onMessageReceived.invoke(message)
            }
            is IpcMessage.LaunchNext -> {
                onMessageReceived.invoke(message)
            }
            is IpcMessage.ShowLaunchButton -> {
                onMessageReceived.invoke(message)
            }
            is IpcMessage.HideLaunchButton -> {
                onMessageReceived.invoke(message)
            }
        }
    }

    private fun IpcMessage.toSystemMessage(): Message {
        return Message
            .obtain(
                null,
                this.toMessageCode().ordinal,
            )
            .apply {
                replyTo = messenger
            }
    }

    private fun IpcMessage.toMessageCode(): IpcMessageCode {
        return when (this) {
            is IpcMessage.RegisterClient -> IpcMessageCode.REGISTER_CLIENT
            is IpcMessage.UnregisterClient -> IpcMessageCode.UNREGISTER_CLIENT
            is IpcMessage.LaunchNext -> IpcMessageCode.LAUNCH_NEXT
            is IpcMessage.StopService -> IpcMessageCode.STOP_SERVICE
            is IpcMessage.StopChain -> IpcMessageCode.STOP_CHAIN
            is IpcMessage.HideLaunchButton -> IpcMessageCode.HIDE_LAUNCH_BUTTON
            is IpcMessage.ShowLaunchButton -> IpcMessageCode.SHOW_LAUNCH_BUTTON
        }
    }
}