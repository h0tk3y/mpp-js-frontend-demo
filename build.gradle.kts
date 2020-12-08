@file:Suppress("UNUSED_VARIABLE")

import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpack

plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("application")
}

repositories {
    maven("https://kotlin.bintray.com/kotlin-js-wrappers")
    jcenter()
}

val ktorVersion = "1.4.3"
val kotlinxSerializationVersion = "1.0.1"
val logbackVersion = "1.2.3"
val kotlinReactVersion = "17.0.0"
val kotlinReactMavenVersion = "17.0.0-pre.129-kotlin-1.4.20"

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
                implementation("io.ktor:ktor-client-core:$ktorVersion")
                implementation("io.ktor:ktor-client-json:$ktorVersion")
                implementation("io.ktor:ktor-client-serialization:$ktorVersion")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$kotlinxSerializationVersion")
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
                implementation("io.ktor:ktor-serialization:$ktorVersion")
                implementation("io.ktor:ktor-server-netty:$ktorVersion")
                implementation("io.ktor:ktor-html-builder:$ktorVersion")
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
                implementation(npm("text-encoding", "0.7.0"))
                implementation(npm("abort-controller", "3.0.0"))

                implementation("org.jetbrains:kotlin-react:$kotlinReactMavenVersion")
                implementation("org.jetbrains:kotlin-react-dom:$kotlinReactMavenVersion")
                implementation(npm("react", "^$kotlinReactVersion"))
                implementation(npm("react-dom", "^$kotlinReactVersion"))
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

val jsBrowserDevelopmentWebpack by tasks.withType<KotlinWebpack>().existing
kotlin.jvm().compilations["main"].defaultSourceSet {
    resources.srcDirs(jsBrowserDevelopmentWebpack.map { it.destinationDirectory })
}