import org.jetbrains.compose.compose
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.10"
    id("org.jetbrains.compose") version "1.0.1-rc2"
    id("com.github.ben-manes.versions") version "0.41.0"
}

description = "jactor::blackjack-application"
group = "com.gitlab.jactor.blackjack.compose"
version = "0.0.0-SNAPSHOT"

repositories {
    google()

    mavenCentral()
    mavenLocal()

    maven(url = "https://maven.pkg.jetbrains.space/public/p/compose/dev")
    maven(url = "https://jitpack.io")
}

dependencies {
    implementation(compose.desktop.currentOs)
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.13.1")
    implementation("org.springframework:spring-context:5.3.15")
    implementation("org.springframework:spring-core:5.3.15")
    implementation("org.springframework:spring-web:5.3.15")

    testImplementation("org.mockito.kotlin:mockito-kotlin:4.0.0")
    testImplementation("org.springframework.boot:spring-boot-starter-test:2.6.3")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "16"
}

compose.desktop {
    application {
        mainClass = "MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "app-blackjack"
            packageVersion = "1.0.0"
        }
    }
}