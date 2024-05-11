plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.jetbrains.compose.compiler)
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

    api(libs.mviKotlin.core)
    api(libs.mviKotlin.main)
    api(libs.mviKotlin.logging)
    api(libs.mviKotlin.timetravel)
    api(libs.mviKotlin.coroutines)

    api(libs.kotlinx.coroutines.core)

    api(libs.koin.core)

    api(libs.activity.compose)
    api(platform(libs.compose.bom))
    api(libs.ui)
    api(libs.ui.graphics)
    api(libs.ui.tooling.preview)
    api(libs.material3)
    debugApi(libs.ui.tooling)
    debugApi(libs.ui.test.manifest)

    api(libs.kotlin.collections)
}