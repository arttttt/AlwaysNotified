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
        val title: String
        val name: String
        val pkg: String

        @Serializable
        data class Service(
            override val title: String,
            override val name: String,
            override val pkg: String,
        ) : Component

        @Serializable
        data class ContentProvider(
            override val title: String,
            override val name: String,
            override val pkg: String,
            val authority: String?,
        ) : Component
    }
}