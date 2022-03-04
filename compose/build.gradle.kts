import org.jetbrains.compose.compose
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.jetbrains.compose") version Library.Version.kotlinCompose
}

description = "blackjack:compose-application"
group = "com.github.jactor.blackjack.compose"
version = "0.0.x-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    google()
    maven(url = "https://maven.pkg.jetbrains.space/public/p/compose/dev")
    maven(url = "https://jitpack.io")
}

dependencies {
    implementation(compose.desktop.currentOs)

    // internal project dependency
    implementation(project(":dto"))

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:${Library.Version.fasterXmlJacksonModule}")
    implementation("org.springframework:spring-context:${Library.Version.springFramework}")
    implementation("org.springframework:spring-core:${Library.Version.springFramework}")
    implementation("org.springframework:spring-web:${Library.Version.springFramework}")

    testImplementation("org.springframework.boot:spring-boot-starter-test:${Library.Version.springBoot}")
    testImplementation(kotlin("test"))
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
