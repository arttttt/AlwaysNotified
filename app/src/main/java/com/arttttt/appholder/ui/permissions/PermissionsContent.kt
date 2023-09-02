package com.arttttt.appholder.ui.permissions

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.jetpack.subscribeAsState
import com.arttttt.appholder.ui.custom.EqualHeightColumn
import com.arttttt.appholder.ui.permissions.content.DeniedPermissionContent
import com.arttttt.appholder.ui.permissions.content.GrantedPermissionContent
import com.arttttt.appholder.ui.permissions.models.PermissionLazyListItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PermissionsContent(component: PermissionsComponent) {
    val state by component.state.subscribeAsState()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        TopAppBar(
            title = { 
                Text(text = "Permissions")
            }
        )

        EqualHeightColumn(
            modifier = Modifier.fillMaxSize(),
            spacing = 8.dp,
        ) {
            state.items.forEach { item ->
                when (item) {
                    is PermissionLazyListItem.Granted -> {
                        key(item.key) {
                            GrantedPermissionContent(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        horizontal = 16.dp,
                                    ),
                                title = item.title,
                            )
                        }
                    }
                    is PermissionLazyListItem.Denied -> {
                        key(item.key) {
                            DeniedPermissionContent(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        horizontal = 16.dp,
                                    ),
                                title = item.title,
                                onClick = remember {
                                    {
                                        component.grantPermissionClicked(item.permission)
                                    }
                                },
                            )
                        }
                    }
                }
            }
        }
    }
}