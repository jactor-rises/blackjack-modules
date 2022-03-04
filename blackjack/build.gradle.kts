import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version Library.Version.springBoot
    id("io.spring.dependency-management") version Library.Version.springDependencyManagement

    kotlin("plugin.spring") version Library.Version.springPlugin
}

description = "blackjack:backend"
group = "com.github.jactor.blackjack"
version = "0.0.x-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

dependencies {
    // spring-boot
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-aop")
    implementation("org.springframework.boot:spring-boot-starter-web")

    // kotlin
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    // internal project dependency
    implementation(project(":dto"))

    // dependencies
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation(Library.Dependencies.springDocOpenApi)

    // test dependencies
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation(Library.Dependencies.mockitoKotlin)
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "17"
    }
}
