plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlinAndroid)
}

android {
    namespace = "com.arttttt.core.arch"
    compileSdk = 34

    defaultConfig {
        minSdk = 33
    }

    buildTypes {
        release {
            isMinifyEnabled = false
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
}

dependencies {
    api(libs.decompose)
    api(libs.decompose.compose)
    api(libs.essenty.lifecycle.lib)
    api(libs.essenty.lifecycle.coroutines)
    api(libs.essenty.backHandler)
    api(libs.essenty.instanceKeeper)
    api(libs.essenty.stateKeeper)

    api(libs.kotlinx.coroutines.core)

    api(libs.koin.core)
}