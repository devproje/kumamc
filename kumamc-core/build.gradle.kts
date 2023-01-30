import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

tasks {
    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "17"
    }

    processResources {
        filesMatching("server.properties") {
            expand(project.properties)
        }
    }
}