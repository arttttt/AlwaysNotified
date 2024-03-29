package com.arttttt.alwaysnotified.ui.settings

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import com.arttttt.alwaysnotified.components.settings.SettingsComponent

@Composable
fun SettingsContent(component: SettingsComponent) {

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {

        Text(
            text = "under construction",
            fontSize = 30.sp,
        )
    }
}