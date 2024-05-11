plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.jetbrains.compose.compiler)
}

android {
    namespace = "com.arttttt.profiles.impl"
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
    implementation(project(":localization"))
    implementation(project(":uikit"))
    implementation(project(":core:arch"))
    implementation(project(":core:appslist"))
    implementation(project(":core:database"))
    implementation(project(":core:lazylist"))
    implementation(project(":feature:profiles:api"))
}