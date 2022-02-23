import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.6.3"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"

    kotlin("plugin.spring") version "1.6.10"
}

description = "blackjack:backend"
group = "com.github.jactor.blackjack"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

dependencies {
    // spring-boot
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-aop")
    implementation("org.springframework.boot:spring-boot-starter-web")

    // kotlin
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    // dependencies

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.springdoc:springdoc-openapi-ui:1.6.6")

    // test dependencies
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "17"
    }
}

tasks.test {
    useJUnitPlatform()

    testLogging {
        lifecycle {
            events = mutableSetOf(TestLogEvent.FAILED, TestLogEvent.PASSED, TestLogEvent.SKIPPED)
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
    addTestListener(BlackjackTestListener())
}

class BlackjackTestListener : TestListener {
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

            failedTests.takeIf { it.isNotEmpty() }?.prefixedSummary("|\tFailed Tests")
            skippedTests.takeIf { it.isNotEmpty() }?.prefixedSummary("|\tSkipped Tests:")
        }
    }

    private infix fun List<TestDescriptor>.prefixedSummary(subject: String) {
        logger.lifecycle(subject)
        forEach { test -> logger.lifecycle("|\t\t${test.className}: ${test.displayName()}") }
    }

    private fun TestDescriptor.displayName() = parent?.name ?: name
}
