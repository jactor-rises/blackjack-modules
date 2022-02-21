import org.jetbrains.compose.compose
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.10"
    id("org.jetbrains.compose") version "1.0.1-rc2"
    id("com.github.ben-manes.versions") version "0.42.0"
}

description = "blackjack:compose-application"
group = "com.gitlab.jactor.blackjack.compose"
version = "0.0.0-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

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

    testLogging {
        lifecycle {
            events = mutableSetOf(
                org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED,
                org.gradle.api.tasks.testing.logging.TestLogEvent.PASSED,
                org.gradle.api.tasks.testing.logging.TestLogEvent.SKIPPED
            )
            exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
            showExceptions = true
            showCauses = true
            showStackTraces = true
            showStandardStreams = true
        }

        info.events = lifecycle.events
        info.exceptionFormat = lifecycle.exceptionFormat
    }

    // Se https://github.com/gradle/kotlin-dsl/issues/836
    addTestListener(TestListenerImpl())
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

class TestListenerImpl : TestListener {
    private val failedTests = mutableListOf<TestDescriptor>()
    private val skippedTests = mutableListOf<TestDescriptor>()

    override fun beforeSuite(suite: TestDescriptor) {}
    override fun beforeTest(testDescriptor: TestDescriptor) {}
    override fun afterTest(testDescriptor: TestDescriptor, result: TestResult) {
        when (result.resultType) {
            TestResult.ResultType.FAILURE -> failedTests.add(testDescriptor)
            TestResult.ResultType.SKIPPED -> skippedTests.add(testDescriptor)
            else -> Unit
        }
    }

    override fun afterSuite(suite: TestDescriptor, result: TestResult) {
        if (suite.parent == null) { // root suite
            logger.lifecycle("")
            logger.lifecycle("/=======================================")
            logger.lifecycle("| Test result: ${result.resultType}")
            logger.lifecycle("|=======================================")

            logger.lifecycle(
                "| Test summary: ${result.testCount} tests, ${result.successfulTestCount} succeeded, ${result.failedTestCount} failed, ${result.skippedTestCount} skipped"
            )

            failedTests.takeIf { it.isNotEmpty() }?.prefixedSummary("\tFailed Tests")
            skippedTests.takeIf { it.isNotEmpty() }?.prefixedSummary("\tSkipped Tests:")
        }
    }

    private infix fun List<TestDescriptor>.prefixedSummary(subject: String) {
        logger.lifecycle(subject)
        forEach { test -> logger.lifecycle("\t\t${test.className}: ${test.displayName()}") }
    }

    private fun TestDescriptor.displayName() = parent?.name ?: name
}
