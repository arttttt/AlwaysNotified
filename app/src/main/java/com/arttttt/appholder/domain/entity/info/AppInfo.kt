package com.arttttt.appholder.domain.entity.info

import kotlinx.serialization.Serializable

@Serializable
data class AppInfo(
    val title: String,
    val pkg: String,
    /**
     * todo: replace by map
     */
    val activities: List<ActivityInfo>,
    val isExpanded: Boolean,
)