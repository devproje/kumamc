plugins {
    id("com.github.johnrengelman.shadow") version "7.1.2"
    application
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
            attributes["Main-Class"] = "net.projecttl.kuma.mc.CoreKt"
        }
    }
}

application {
    mainClass.set("net.projecttl.kuma.mc.CoreKt")
}
