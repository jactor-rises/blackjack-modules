import org.gradle.api.tasks.testing.logging.TestLogEvent

plugins {
    kotlin("jvm")
}

repositories {
    gradlePluginPortal()
    mavenCentral()
    mavenLocal()
}

tasks {
    compileKotlin {
        kotlinOptions {
            allWarningsAsErrors = true
            jvmTarget = "17"
        }
    }
}

java.sourceCompatibility = JavaVersion.VERSION_17
java.targetCompatibility = JavaVersion.VERSION_17

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
