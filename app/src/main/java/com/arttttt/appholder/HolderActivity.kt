package com.arttttt.appholder

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.core.os.postDelayed
import java.io.Serializable
import java.util.LinkedList

class HolderActivity : ComponentActivity() {

    companion object {

        const val APPS_TO_START = "payload"
    }

    private val handler by lazy {
        Handler(Looper.getMainLooper())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val appsToStart = intent
            .getSerializable<ArrayList<Intent>?>(APPS_TO_START)
            ?.let(::LinkedList)
            ?: LinkedList()

        if (appsToStart.isEmpty()) {
            moveTaskToBack(true)
        } else {
            val appToStart = appsToStart.poll()!!

            startActivityForResult(appToStart, 0)

            startActivity(
                intent<TestActivity> {
                    putExtra(
                        TestActivity.APP_TITLE, "Implementation required"
                    )
                }
            )

            startNextActivity(appsToStart)
        }
    }

    private fun startNextActivity(
        appsToStart: LinkedList<Intent>,
    ) {
        handler.postDelayed(1000) {
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