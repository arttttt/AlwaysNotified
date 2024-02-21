package com.arttttt.alwaysnotified

import android.app.ActivityManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.os.Messenger
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContract
import androidx.core.os.postDelayed
import com.arttttt.alwaysnotified.utils.extensions.getSerializable
import com.arttttt.alwaysnotified.utils.extensions.intent
import com.arttttt.alwaysnotified.utils.ipc.AppsServiceIpcMessenger
import timber.log.Timber
import java.io.Serializable
import java.lang.Exception
import java.util.LinkedList
import kotlin.system.exitProcess

class HolderActivity : ComponentActivity() {

    private class StartPayloadContract : ActivityResultContract<Intent, Unit>() {

        override fun createIntent(context: Context, input: Intent): Intent {
            return input
        }

        override fun parseResult(resultCode: Int, intent: Intent?) {
            // do nothing
        }
    }

    companion object {

        const val APPS_TO_START = "payload"
        const val TARGET_TITLE = "title"
        const val MANUAL_MODE = "manual_mode"

        private const val FINISH_CHAIN = "finish_chain"
    }

    private val handler by lazy {
        Handler(Looper.getMainLooper())
    }

    private val startPayloadLauncher = registerForActivityResult(StartPayloadContract()) {}

    private val messenger = AppsServiceIpcMessenger(
        tag = "holder",
        onMessageReceived = ::handleMessage
    )

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(
            name: ComponentName,
            service: IBinder,
        ) {
            try {
                messenger.sendMessage(
                    AppsServiceIpcMessenger.IpcMessage.RegisterClient(Messenger(service))
                )

                messenger.sendMessage(
                    if (manualMode) {
                        AppsServiceIpcMessenger.IpcMessage.ShowLaunchButton
                    } else {
                        AppsServiceIpcMessenger.IpcMessage.HideLaunchButton
                    }
                )
            } catch (_: Exception) {}
        }

        override fun onServiceDisconnected(name: ComponentName) {
            messenger.sendMessage(AppsServiceIpcMessenger.IpcMessage.UnregisterClient)
        }
    }

    private val appsToStart: LinkedList<Intent> by lazy {
        intent
            .getSerializable<ArrayList<Intent>?>(APPS_TO_START)
            ?.let(::LinkedList)
            ?: LinkedList()
    }

    private val appToLaunch by lazy {
        appsToStart.poll()!!
    }

    private val manualMode: Boolean
        get() {
            return appToLaunch.getBooleanExtra(MANUAL_MODE, false)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        when {
            intent.getBooleanExtra(FINISH_CHAIN, false) -> finishAndRemoveTask()
            else -> {
                bindService(
                    Intent(
                        applicationContext,
                        AppStartupService::class.java,
                    ),
                    serviceConnection,
                    Context.BIND_AUTO_CREATE
                )

                setTaskDescription(
                    ActivityManager.TaskDescription
                        .Builder()
                        .setLabel(getString(R.string.do_not_close))
                        .build()
                )

                launchApp()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        runCatching {
            unbindService(serviceConnection)
        }
    }

    private fun handleMessage(message: AppsServiceIpcMessenger.IpcMessage) {
        when (message) {
            is AppsServiceIpcMessenger.IpcMessage.LaunchNext -> {
                startActivity(
                    intent<ProtectorActivity>()
                )

                startNextActivity(
                    appsToStart = appsToStart,
                )
            }
            is AppsServiceIpcMessenger.IpcMessage.StopChain -> {
                startActivity(
                    intent<HolderActivity> {
                        flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP

                        putExtra(FINISH_CHAIN, true)
                    }
                )
            }
            else -> {}
        }
    }

    private fun launchApp() {
        if (appsToStart.isEmpty()) {
            moveTaskToBack(true)
            finish()
        } else {
            val isErrorOccurred = kotlin
                .runCatching {
                    startPayloadLauncher.launch(appToLaunch)

                    false
                }
                .onFailure { e ->
                    Toast.makeText(
                        applicationContext,
                        getString(
                            R.string.can_not_launch_activity,
                            appToLaunch.getStringExtra(TARGET_TITLE),
                        ),
                        Toast.LENGTH_SHORT,
                    ).show()

                    Timber.e(e)
                }
                .getOrDefault(true)

            when {
                isErrorOccurred -> {
                    finish()

                    startNextActivity(appsToStart)
                }
                manualMode -> {
                    /**
                     * do nothing
                     * wait for a command
                     */
                }
                else -> {
                    startActivity(
                        intent<ProtectorActivity>()
                    )

                    handler.postDelayed(
                        3000,
                    ) {
                        startNextActivity(
                            appsToStart = appsToStart,
                        )
                    }
                }
            }
        }
    }

    private fun startNextActivity(
        appsToStart: LinkedList<Intent>,
    ) {
        if (appsToStart.isEmpty()) {
            moveTaskToBack(true)
            messenger.sendMessage(AppsServiceIpcMessenger.IpcMessage.StopService)
        } else {
            startActivity(
                intent<HolderActivity> {
                    putExtra(
                        APPS_TO_START,
                        appsToStart as Serializable,
                    )
                }
            )

            unbindService(serviceConnection)
        }
    }
}