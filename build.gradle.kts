import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.5.10")
        classpath("com.android.tools.build:gradle:4.2.2")
        classpath("com.squareup.sqldelight:gradle-plugin:1.5.0")
        classpath("com.codingfeline.buildkonfig:buildkonfig-gradle-plugin:0.7.1")
    }
}

plugins {
    kotlin("multiplatform") version "1.5.10"
    kotlin("native.cocoapods") version "1.5.10"
    kotlin("plugin.serialization") version "1.5.10"
    id("com.squareup.sqldelight") version "1.5.0"
    id("com.codingfeline.buildkonfig") version "0.7.1"
    id("com.android.library") version "4.2.2"
}

version = "1.0"

repositories {
    google()
    mavenCentral()
}

kotlin {
    android()

    val iosTarget: (String, org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget.() -> Unit) -> org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget =
        if (System.getenv("SDK_NAME")?.startsWith("iphoneos") == true)
            ::iosArm64
        else
            ::iosX64

    iosTarget("ios") {}

    cocoapods {
        summary = "Dogify Shared module"
        homepage = "https://github.com/nrobi144/dogify-shared"
        ios.deploymentTarget = "14.1"
        frameworkName = "shared"
    }

    sourceSets {
        val ktorVersion = "1.6.0"
        val sqlDelightVersion = "1.5.0"
        val koinVersion = "3.1.1"

        val commonMain by getting {
            dependencies {
                // Coroutines
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.0-native-mt")
                // Ktor
                implementation("io.ktor:ktor-client-core:$ktorVersion")
                implementation("io.ktor:ktor-client-json:$ktorVersion")
                implementation("io.ktor:ktor-client-logging:$ktorVersion")
                implementation("io.ktor:ktor-client-serialization:$ktorVersion")
                // Serialization
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.2.1")
                // Sql Delight
                implementation("com.squareup.sqldelight:runtime:$sqlDelightVersion")
                implementation("com.squareup.sqldelight:coroutines-extensions:$sqlDelightVersion")
                // Koin
                api("io.insert-koin:koin-core:$koinVersion")
                api("io.insert-koin:koin-test:$koinVersion")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
        val androidMain by getting {
            dependencies {
                implementation("io.ktor:ktor-client-android:$ktorVersion")
                implementation("com.squareup.sqldelight:android-driver:$sqlDelightVersion")
            }
        }
        val androidTest by getting
        val iosMain by getting {
            dependencies {
                implementation("io.ktor:ktor-client-ios:$ktorVersion")
                implementation("com.squareup.sqldelight:native-driver:$sqlDelightVersion")
            }
        }
        val iosTest by getting
    }
}

android {
    compileSdkVersion(30)
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdkVersion(23)
        targetSdkVersion(30)
    }
}

buildkonfig {
    packageName = "com.nagyrobi144.dogify"
    defaultConfigs {
        buildConfigField(STRING, "baseUrl", "https://dog.ceo")
    }
}
