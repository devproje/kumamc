plugins {
    id("com.github.johnrengelman.shadow") version "7.1.2"
    application
}

group = rootProject.group
version = rootProject.version

dependencies {
    implementation(project(":kumamc-core"))
}

tasks {
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions.jvmTarget = "17"
    }
}

application {
    mainClass.set("net.projecttl.kuma.mc.runner.MainKt")
}