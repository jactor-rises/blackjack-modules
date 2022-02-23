import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.jetbrains.kotlin.jvm") version "1.6.10" apply false
    id("com.github.ben-manes.versions") version "0.42.0" apply false
}

subprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "com.github.ben-manes.versions")

    repositories {
        gradlePluginPortal()
        mavenCentral()
        mavenLocal()
    }

    dependencies {
        val implementation by configurations
        val testImplementation by configurations
        implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.6.10")
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
