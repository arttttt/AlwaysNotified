@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.kotlinParcelize)
    alias(libs.plugins.kotlinSerialization)
}

android {
    namespace = "com.arttttt.appholder"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.arttttt.appholder"
        minSdk = 33
        targetSdk = 33
        versionCode = 1
        versionName = "developer preview"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"

        freeCompilerArgs = listOf("-Xcontext-receivers")
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(libs.core.ktx)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.activity.compose) {
        exclude(group = "androidx.emoji2", module = "emoji2")
        exclude(group = "androidx.emoji2", module = "emoji2-views-helper")
    }
    implementation(platform(libs.compose.bom))
    implementation(libs.ui)
    implementation(libs.ui.graphics)
    implementation(libs.ui.tooling.preview)
    implementation(libs.material3)
    debugImplementation(libs.ui.tooling)
    debugImplementation(libs.ui.test.manifest)

    implementation(libs.koin.core)
    implementation(libs.koin.android)

    implementation(libs.decompose)
    implementation(libs.decompose.jetpackCompose)
    implementation(libs.essenty.lifecycle)
    implementation(libs.essenty.backHandler)
    implementation(libs.essenty.parcelable)
    implementation(libs.essenty.instanceKeeper)
    implementation(libs.essenty.stateKeeper)

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
    implementation(libs.mviKotlin.rx)
}