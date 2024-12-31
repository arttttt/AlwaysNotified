package com.arttttt.appslist.api

import kotlinx.serialization.Serializable

@Serializable
data class AppInfo(
    val title: String,
    val pkg: String,
    val components: List<Component>,
) {

    @Serializable
    sealed interface Component : Comparable<Component> {
        val title: String
        val name: String
        val pkg: String

        override fun compareTo(other: Component): Int {
            return when {
                this is Service && other is Service -> 0
                this is ContentProvider && other is ContentProvider -> 0
                this is ContentProvider && other is Service -> -1
                else -> 1
            }
        }

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
            val authority: String,
        ) : Component
    }
}