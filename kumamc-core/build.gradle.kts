import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    application
}

dependencies {
    implementation(project(":kumamc-api"))
}

tasks {
    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "17"
    }

    withType<ShadowJar> {
        archiveBaseName.set(project.name)
        archiveClassifier.set("")
        archiveVersion.set("")

        manifest {
            attributes["Main-Class"] = "net.projecttl.kuma.mc.CoreKt"
        }
    }
}

application {
    mainClass.set("net.projecttl.kuma.mc.CoreKt")
}