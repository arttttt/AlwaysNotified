package com.arttttt.appslist.impl.domain.entity

import kotlinx.serialization.Serializable

@Serializable
internal data class ActivityInfo(
    val title: String,
    val name: String,
    val pkg: String,
)