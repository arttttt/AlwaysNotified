package com.arttttt.alwaysnotified

import kotlinx.serialization.Serializable

@Serializable
data class AppInfo(
    val title: String,
    val pkg: String,
    val activities: Set<ActivityInfo>,
    val isExpanded: Boolean,
)