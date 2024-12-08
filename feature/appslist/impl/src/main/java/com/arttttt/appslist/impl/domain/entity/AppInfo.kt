package com.arttttt.appslist.impl.domain.entity

import kotlinx.serialization.Serializable

@Serializable
data class AppInfo(
    val title: String,
    val pkg: String,
    val components: List<Component>,
) {

    @Serializable
    sealed interface Component {
        val name: String
        val pkg: String

        @Serializable
        data class Service(
            override val name: String,
            override val pkg: String,
        ) : Component

        @Serializable
        data class BroadcastReceiver(
            override val name: String,
            override val pkg: String,
        ) : Component

        @Serializable
        data class ContentProvider(
            override val name: String,
            override val pkg: String,
        ) : Component
    }
}