package com.arttttt.profiles.impl.ui.profileactions

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.ui.graphics.vector.ImageVector

sealed class ProfileAction {

    abstract val title: String
    abstract val icon: ImageVector

    data object Rename : ProfileAction() {
        override val title: String = "Rename"
        override val icon: ImageVector = Icons.Default.Edit
    }

    data object Remove : ProfileAction() {
        override val title: String = "Remove"
        override val icon: ImageVector = Icons.Default.Delete
    }
}