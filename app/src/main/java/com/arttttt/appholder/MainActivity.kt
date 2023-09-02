package com.arttttt.appholder

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import com.arkivanov.decompose.defaultComponentContext
import com.arttttt.appholder.ui.root.RootComponent
import com.arttttt.appholder.ui.root.RootComponentImpl
import com.arttttt.appholder.ui.root.RootContent

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val rootComponent: RootComponent = RootComponentImpl(
            componentContext = defaultComponentContext(),
        )

        setContent {
            MaterialTheme {
                RootContent(
                    component = rootComponent,
                )
            }
        }

        requestPermissions(
            arrayOf(
                Manifest.permission.QUERY_ALL_PACKAGES,
                Manifest.permission.POST_NOTIFICATIONS,
            ),
            0,
        )
    }
}