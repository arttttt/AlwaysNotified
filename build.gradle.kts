import org.xml.sax.InputSource
import java.io.StringReader
import javax.xml.parsers.DocumentBuilderFactory

plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.kotlinAndroid) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.kotlinSerialization) apply false
    alias(libs.plugins.google.services) apply false
    alias(libs.plugins.firebase.crashlytics) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.jetbrains.compose.compiler) apply false
}

tasks.register("printVersion") {
    doLast {
        val manifestFile = file("app/build/intermediates/merged_manifests/release/AndroidManifest.xml").readText()
        val xml = DocumentBuilderFactory
            .newInstance()
            .newDocumentBuilder()
            .parse(InputSource(StringReader(manifestFile)))

        val versionCode = xml.documentElement.getAttribute("android:versionCode")
        val versionName = xml.documentElement.getAttribute("android:versionName")

        println("Version Name: $versionName")
        println("Version Code: $versionCode")
    }
}
