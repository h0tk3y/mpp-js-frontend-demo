@file:Suppress("UNUSED_VARIABLE")

import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpack

plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("application")
}

repositories {
    maven("https://kotlin.bintray.com/kotlin-dev")
    maven("https://kotlin.bintray.com/ktor")
    maven("https://kotlin.bintray.com/kotlin-js-wrappers")
    jcenter()
}

val ktorVersion = "1.2.6"
val logbackVersion = "1.2.3"

kotlin {
    jvm {
        withJava()
    }
    js {
        browser()
    }
    sourceSets {
        commonMain {
            dependencies {
                implementation(kotlin("stdlib-common"))
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime-common:0.14.0")
            }
        }
        commonTest {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
        val jvmMain by getting {
            dependencies {
                implementation(kotlin("stdlib-jdk8"))
                implementation("io.ktor:ktor-server-netty:$ktorVersion")
                implementation("io.ktor:ktor-html-builder:$ktorVersion")
                implementation("io.ktor:ktor-client-json-jvm:$ktorVersion")
                implementation("io.ktor:ktor-serialization:$ktorVersion")
                implementation("ch.qos.logback:logback-classic:$logbackVersion")
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation(kotlin("test-junit"))
            }
        }
        val jsMain by getting {
            dependencies {
                implementation(kotlin("stdlib-js"))
                implementation("io.ktor:ktor-client-js:$ktorVersion")
                implementation("io.ktor:ktor-client-json-js:$ktorVersion")
                implementation("io.ktor:ktor-client-serialization-js:$ktorVersion")
                implementation(npm("text-encoding", "0.7.0"))
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime-js:0.14.0")

                implementation("org.jetbrains:kotlin-react:16.9.0-pre.89-kotlin-1.3.60")
                implementation("org.jetbrains:kotlin-react-dom:16.9.0-pre.89-kotlin-1.3.60")
                implementation(npm("react", "^16.12.0"))
                implementation(npm("react-dom", "^16.12.0"))
            }
        }
        val jsTest by getting {
            dependencies {
                implementation(kotlin("test-js"))
            }
        }

        all {
            languageSettings.enableLanguageFeature("InlineClasses")
        }
    }
}

application {
    mainClassName = "com.h0tk3y.mpp.sample.server.ServerKt"
}

val jvmProcessResources by tasks.withType(ProcessResources::class).getting {
    val jsWebpackTaskName =
        if (project.findProperty("production") == "true")
            "jsBrowserProductionWebpack"
        else
            "jsBrowserDevelopmentWebpack"
    val jsWebpackTask = tasks.withType(KotlinWebpack::class).named(jsWebpackTaskName)
    from(jsWebpackTask.map { project.files(it.destinationDirectory) })
}