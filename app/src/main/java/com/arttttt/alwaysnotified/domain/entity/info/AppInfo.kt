package com.arttttt.alwaysnotified.domain.entity.info

import kotlinx.serialization.Serializable

@Serializable
data class AppInfo(
    val title: String,
    val pkg: String,
    val activities: Set<ActivityInfo>,
    val isExpanded: Boolean,
)