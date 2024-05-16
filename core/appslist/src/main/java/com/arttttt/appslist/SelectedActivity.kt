package com.arttttt.appslist

import kotlinx.serialization.Serializable

@Serializable
data class SelectedActivity(
    val pkg: String,
    val name: String,
    val manualMode: Boolean,
)