plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.google.services)
    alias(libs.plugins.firebase.crashlytics)
}

android {
    namespace = "com.arttttt.alwaysnotified"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.arttttt.alwaysnotified"
        minSdk = 33
        targetSdk = 34
        versionCode = 8
        versionName = "0.0.8"

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
        compose = true
        buildConfig = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(project(":uikit"))
    implementation(project(":localization"))
    implementation(project(":core:arch"))
    implementation(project(":core:alwaysnotified"))
    implementation(project(":core:lazylist"))
    implementation(project(":feature:profiles:api"))
    implementation(project(":feature:profiles:impl"))
    implementation(project(":feature:appssearch:api"))
    implementation(project(":feature:appssearch:impl"))
    implementation(project(":feature:topbar:api"))
    implementation(project(":feature:topbar:impl"))
    implementation(project(":feature:appslist:api"))
    implementation(project(":feature:appslist:impl"))
    implementation(project(":feature:permissions:api"))
    implementation(project(":feature:permissions:impl"))

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

    implementation(libs.mviKotlin.core)
    implementation(libs.mviKotlin.main)
    implementation(libs.mviKotlin.logging)
    implementation(libs.mviKotlin.timetravel)
    implementation(libs.mviKotlin.coroutines)

    implementation(libs.flowext)

    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)

    implementation(libs.accompanist.drawablepainter)

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.crashlytics)
    implementation(libs.firebase.analytics)

    implementation(libs.timber)
}