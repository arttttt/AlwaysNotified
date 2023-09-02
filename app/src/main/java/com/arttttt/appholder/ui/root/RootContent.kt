package com.arttttt.appholder.ui.root

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.jetpack.stack.Children
import com.arttttt.appholder.ui.appslist.AppListComponent
import com.arttttt.appholder.ui.appslist.AppsListContent
import com.arttttt.appholder.ui.permissions.PermissionsComponent
import com.arttttt.appholder.ui.permissions.PermissionsContent

@Composable
fun RootContent(component: RootComponent) {
    Children(
        stack = component.stack
    ) { child ->
        when (val instance = child.instance) {
            is AppListComponent -> AppsListContent(instance)
            is PermissionsComponent -> PermissionsContent(instance)
        }
    }
}