pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Always Notified"
include(":app")
include(":core:arch")
include(":feature:profiles:api")
include(":feature:profiles:impl")
include(":uikit")
include(":localization")
include(":core:lazylist")
include(":feature:appssearch:api")
include(":feature:appssearch:impl")
include(":feature:topbar:api")
include(":feature:topbar:impl")
