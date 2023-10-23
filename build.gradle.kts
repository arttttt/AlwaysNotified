import org.xml.sax.InputSource
import java.io.StringReader
import javax.xml.parsers.DocumentBuilderFactory

// Top-level build file where you can add configuration options common to all sub-projects/modules.
@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.kotlinAndroid) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.kotlinParcelize) apply false
    alias(libs.plugins.kotlinSerialization) apply false
}
true // Needed to make the Suppress annotation work for the plugins block

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
