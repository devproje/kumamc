plugins {
    id("com.github.johnrengelman.shadow") version "8.1.1"
    application
}

dependencies {
    implementation(project(":kumamc-api"))
    implementation(project(":kumamc-core"))
}

tasks {
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions.jvmTarget = "17"
    }

    processResources {
        filesMatching("server.properties") {
            expand(project.properties)
        }
    }

    shadowJar {
        archiveVersion.set(rootProject.version.toString())
        archiveBaseName.set(rootProject.name)
        archiveClassifier.set("")

        manifest {
            attributes["Main-Class"] = "net.projecttl.kuma.mc.runner.MainKt"
        }
    }
}

application {
    mainClass.set("net.projecttl.kuma.mc.runner.MainKt")
}