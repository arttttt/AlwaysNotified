package com.arttttt.appslist.impl.domain.entity

import kotlinx.serialization.Serializable

@Serializable
internal data class AppInfo(
    val title: String,
    val pkg: String,
    val activities: Set<ActivityInfo>,
    val isExpanded: Boolean,
)