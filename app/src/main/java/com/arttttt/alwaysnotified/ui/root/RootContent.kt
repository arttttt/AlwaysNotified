package com.arttttt.alwaysnotified.ui.root

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arttttt.appslist.api.AppsListComponent
import com.arttttt.alwaysnotified.components.permissions.PermissionsComponent
import com.arttttt.alwaysnotified.components.root.RootComponent
import com.arttttt.alwaysnotified.components.settings.SettingsComponent
import com.arttttt.alwaysnotified.ui.permissions.PermissionsContent
import com.arttttt.alwaysnotified.ui.settings.SettingsContent

@Composable
fun RootContent(component: RootComponent) {
    Children(
        stack = component.stack
    ) { child ->
        when (val instance = child.instance) {
            is AppsListComponent -> instance.content.Content(modifier = Modifier)
            is PermissionsComponent -> PermissionsContent(instance)
            is SettingsComponent -> SettingsContent(instance)
        }
    }
}