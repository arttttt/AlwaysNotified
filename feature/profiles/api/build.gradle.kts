plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlinAndroid)
}

android {
    namespace = "com.arttttt.profiles.api"
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
    implementation(project(":core:arch"))
    implementation(project(":core:appslist"))
    implementation(project(":core:lazylist"))
}