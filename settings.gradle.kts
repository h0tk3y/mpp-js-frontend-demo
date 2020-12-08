pluginManagement {
    repositories {
        maven("https://kotlin.bintray.com/kotlin-dev")
        gradlePluginPortal()
    }
    plugins {
        val kotlinVersion = "1.4.20"
        kotlin("multiplatform").version(kotlinVersion)
        kotlin("plugin.serialization").version(kotlinVersion)
    }
}

rootProject.name = "mpp-js-frontend-demo"

