pluginManagement {
    repositories {
        maven("https://kotlin.bintray.com/kotlin-dev")
        gradlePluginPortal()
    }
    plugins {
        kotlin("multiplatform").version("1.3.70-dev-2837")
        kotlin("plugin.serialization").version("1.3.70-dev-2837")
    }
}

rootProject.name = "mpp-js-frontend-demo"

