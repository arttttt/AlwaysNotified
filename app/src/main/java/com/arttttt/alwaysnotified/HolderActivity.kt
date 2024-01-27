package com.arttttt.alwaysnotified

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContract
import androidx.core.os.postDelayed
import com.arttttt.alwaysnotified.utils.extensions.getSerializable
import com.arttttt.alwaysnotified.utils.extensions.intent
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
    }

    private val handler by lazy {
        Handler(Looper.getMainLooper())
    }

    private val startPayloadLauncher = registerForActivityResult(StartPayloadContract()) {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val appsToStart = intent
            .getSerializable<ArrayList<Intent>?>(APPS_TO_START)
            ?.let(::LinkedList)
            ?: LinkedList()

        if (appsToStart.isEmpty()) {
            moveTaskToBack(true)
            finish()
        } else {
            val appToStart = appsToStart.poll()!!

            startPayloadLauncher.launch(appToStart)

            startActivity(
                intent<ProtectorActivity>()
            )

            startNextActivity(appsToStart)
        }
    }

    private fun startNextActivity(
        appsToStart: LinkedList<Intent>,
    ) {
        handler.postDelayed(3000) {
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