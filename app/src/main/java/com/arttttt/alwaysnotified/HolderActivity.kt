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
import com.arttttt.alwaysnotified.utils.extensions.getSerializable
import com.arttttt.alwaysnotified.utils.extensions.intent
import com.arttttt.alwaysnotified.utils.ipc.AppsServiceIpcMessenger
import timber.log.Timber
import java.io.Serializable
import java.lang.Exception
import java.util.LinkedList

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
            } catch (_: Exception) {}
        }

        override fun onServiceDisconnected(name: ComponentName) {
            messenger.sendMessage(AppsServiceIpcMessenger.IpcMessage.UnregisterClient)
        }
    }

    private var isErrorOccurred: Boolean = false
    private lateinit var appsToStart: LinkedList<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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

    override fun onDestroy() {
        super.onDestroy()

        unbindService(serviceConnection)
    }

    private fun handleMessage(message: AppsServiceIpcMessenger.IpcMessage) {
        when (message) {
            is AppsServiceIpcMessenger.IpcMessage.LaunchNext -> {
                startNextActivity(
                    isErrorOccurred = isErrorOccurred,
                    appsToStart = appsToStart,
                )
            }
            is AppsServiceIpcMessenger.IpcMessage.StopChain -> {
                finishAndRemoveTask()
            }
            else -> {}
        }
    }

    private fun launchApp() {
        val appsToStart = intent
            .getSerializable<ArrayList<Intent>?>(APPS_TO_START)
            ?.let(::LinkedList)
            ?: LinkedList()

        if (appsToStart.isEmpty()) {
            moveTaskToBack(true)
            finish()
        } else {
            val appToStart = appsToStart.poll()!!

            val isErrorOccurred = kotlin
                .runCatching {
                    startPayloadLauncher.launch(appToStart)

                    false
                }
                .onFailure { e ->
                    Toast.makeText(
                        applicationContext,
                        getString(
                            R.string.can_not_launch_activity,
                            appToStart.getStringExtra(TARGET_TITLE),
                        ),
                        Toast.LENGTH_SHORT,
                    ).show()

                    Timber.e(e)
                }
                .getOrDefault(true)

            this.isErrorOccurred = isErrorOccurred
            this.appsToStart = appsToStart
        }
    }

    private fun startNextActivity(
        isErrorOccurred: Boolean,
        appsToStart: LinkedList<Intent>,
    ) {
        if (isErrorOccurred) {
            finish()
        }

        startActivity(
            intent<ProtectorActivity>()
        )

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