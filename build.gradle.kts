import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("com.github.ben-manes.versions") version "0.42.0" apply false
}

subprojects {
    apply(plugin = "kotlin-conventions")
    apply(plugin = "com.github.ben-manes.versions")

    repositories {
        gradlePluginPortal()
        mavenCentral()
        mavenLocal()
    }

    dependencies {
        val testImplementation by configurations
        testImplementation("org.mockito.kotlin:mockito-kotlin:4.0.0")
    }

    tasks {
        named("compileKotlin", KotlinCompile::class) {
            kotlinOptions {
                allWarningsAsErrors = true
            }
        }
    }
}
