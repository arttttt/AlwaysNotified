package com.arttttt.alwaysnotified

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContract
import androidx.core.os.postDelayed
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

    companion object {

        const val APPS_TO_START = "payload"
        const val TARGET_TITLE = "title"
    }

    private val handler by lazy {
        Handler(Looper.getMainLooper())
    }

    private val startPayloadLauncher = registerForActivityResult(StartPayloadContract()) {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setTaskDescription(
            ActivityManager.TaskDescription
                .Builder()
                .setLabel(getString(R.string.do_not_close))
                .build()
        )

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

                    startActivity(
                        intent<ProtectorActivity>()
                    )

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

            startNextActivity(
                isErrorOccurred = isErrorOccurred,
                appsToStart = appsToStart,
            )
        }
    }

    private fun startNextActivity(
        isErrorOccurred: Boolean,
        appsToStart: LinkedList<Intent>,
    ) {
        handler.postDelayed(3000) {
            if (isErrorOccurred) {
                finish()
            }

            if (appsToStart.isEmpty()) {
                moveTaskToBack(true)
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
}