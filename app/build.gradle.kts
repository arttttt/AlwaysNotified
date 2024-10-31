plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.google.services)
    alias(libs.plugins.firebase.crashlytics)
    alias(libs.plugins.jetbrains.compose.compiler)
}

android {
    namespace = "com.arttttt.alwaysnotified"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.arttttt.alwaysnotified"
        minSdk = 33
        targetSdk = 35
        versionCode = 10
        versionName = "0.2.0"

        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        debug {
            applicationIdSuffix = ".dev"
            versionNameSuffix = "-dev"

            resValue("string", "app_name", "@string/app_name_dev")
        }

        release {
            isMinifyEnabled = false

            resValue("string", "app_name", "@string/app_name_release")
            signingConfig = signingConfigs.getByName("debug")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"

        freeCompilerArgs = freeCompilerArgs + listOf("-Xcontext-receivers")

        options.optIn.addAll(
            "kotlinx.coroutines.ExperimentalCoroutinesApi"
        )
    }

    buildFeatures {
        buildConfig = true
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(project(":core:arch"))
    implementation(project(":core:appslist"))
    implementation(project(":core:database"))
    implementation(project(":core:lazylist"))
    implementation(project(":feature:appssearch:api"))
    implementation(project(":feature:appssearch:impl"))
    implementation(project(":feature:topbar:api"))
    implementation(project(":feature:topbar:impl"))
    implementation(project(":feature:appslist:api"))
    implementation(project(":feature:appslist:impl"))
    implementation(project(":feature:permissions:api"))
    implementation(project(":feature:permissions:impl"))
    implementation(project(":localization"))
    implementation(project(":uikit"))
    implementation(project(":utils"))

    implementation(libs.core.ktx)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.activity.compose)
    implementation(platform(libs.compose.bom))
    implementation(libs.ui)
    implementation(libs.ui.graphics)
    implementation(libs.ui.tooling.preview)
    implementation(libs.material3)
    debugImplementation(libs.ui.tooling)
    debugImplementation(libs.ui.test.manifest)

    implementation(libs.koin.core)
    implementation(libs.koin.android)

    implementation(libs.kotlin.collections)

    implementation(libs.datastore.preferences)
    implementation(libs.kotlinx.serialization.json)

    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)

    implementation(libs.flowext)

    implementation(libs.room.runtime)

    implementation(libs.accompanist.drawablepainter)

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.crashlytics)
    implementation(libs.firebase.analytics)

    implementation(libs.timber)
}