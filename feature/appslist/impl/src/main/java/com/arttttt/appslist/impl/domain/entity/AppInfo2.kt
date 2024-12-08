package com.arttttt.appslist.impl.domain.entity

data class AppInfo2(
    val title: String,
    val pkg: String,
    val components: List<Component>,
) {

    sealed interface Component {
        val name: String
        val pkg: String

        data class Service(
            override val name: String,
            override val pkg: String,
        ) : Component

        data class BroadcastReceiver(
            override val name: String,
            override val pkg: String,
        ) : Component

        data class ContentProvider(
            override val name: String,
            override val pkg: String,
        ) : Component
    }
}