plugins {
    kotlin("jvm") version "1.8.10"
}

group = "net.projecttl"
version = "1.0.0-beta.7"

allprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")

    repositories {
        mavenCentral()
    }

    java {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(17))
        }
    }
}

subprojects {
    repositories {
        mavenCentral()
        maven("https://jitpack.io")
    }

    dependencies {
        implementation(kotlin("stdlib"))
        implementation(kotlin("reflect"))
        implementation("org.xerial:sqlite-jdbc:3.40.0.0")
        implementation("com.google.guava:guava:31.1-jre")
        implementation("org.jetbrains.exposed:exposed-core:0.41.1")
        implementation("org.jetbrains.exposed:exposed-jdbc:0.41.1")
        implementation("net.kyori:adventure-text-minimessage:4.12.0")
        implementation("org.apache.logging.log4j:log4j-slf4j-impl:2.19.0")
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
        implementation(dependencyNotation = "com.github.Minestom.Minestom:Minestom:-SNAPSHOT")
    }
}