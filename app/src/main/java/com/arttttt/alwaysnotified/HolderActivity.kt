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
import android.os.Message
import android.os.Messenger
import android.os.RemoteException
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContract
import com.arttttt.alwaysnotified.utils.extensions.getSerializable
import com.arttttt.alwaysnotified.utils.extensions.intent
import timber.log.Timber
import java.io.Serializable
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

    private class MessagesHandler(
        looper: Looper,
        private val onMessageReceived: (AppStartupService.OutcomeMessages) -> Unit,
    ) : Handler(looper) {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                AppStartupService.OutcomeMessages.LAUNCH_NEXT.ordinal -> onMessageReceived.invoke(AppStartupService.OutcomeMessages.LAUNCH_NEXT)
                AppStartupService.OutcomeMessages.STOP_CHAIN.ordinal -> onMessageReceived.invoke(AppStartupService.OutcomeMessages.STOP_CHAIN)
                else -> super.handleMessage(msg)
            }
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

    private var serviceMessenger: Messenger? = null

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(
            name: ComponentName,
            service: IBinder,
        ) {
            val serviceMessenger = Messenger(service)
            try {
                val msg = Message.obtain(null, AppStartupService.IncomeMessages.REGISTER_CLIENT.ordinal)
                msg.replyTo = clientMessenger
                serviceMessenger.send(msg)
                this@HolderActivity.serviceMessenger = serviceMessenger
            } catch (_: RemoteException) {}
        }

        override fun onServiceDisconnected(name: ComponentName) {
            serviceMessenger = null
        }
    }

    private val clientMessenger by lazy {
        Messenger(
            MessagesHandler(
                looper = Looper.myLooper()!!,
                onMessageReceived = ::handleMessage,
            )
        )
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

        serviceMessenger?.send(Message.obtain(null, AppStartupService.IncomeMessages.UNREGISTER_CLIENT.ordinal))
        unbindService(serviceConnection)
    }

    private fun handleMessage(message: AppStartupService.OutcomeMessages) {
        when (message) {
            AppStartupService.OutcomeMessages.LAUNCH_NEXT -> {
                startNextActivity(
                    isErrorOccurred = isErrorOccurred,
                    appsToStart = appsToStart,
                )
            }
            AppStartupService.OutcomeMessages.STOP_CHAIN ->{
                finishAndRemoveTask()
            }
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
            serviceMessenger?.send(Message.obtain(null, AppStartupService.IncomeMessages.STOP_SERVICE.ordinal))
        } else {
            startActivity(
                intent<HolderActivity> {
                    putExtra(
                        APPS_TO_START,
                        appsToStart as Serializable,
                    )
                }
            )
        }
    }
}