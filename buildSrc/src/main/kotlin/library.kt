object BlackjackModules {
    object Version {
        // PS!
        // kotlin language version and kotlin-gradle plugin is an implementation defined in buildSrc/build.gradle.kts

        // Dependencies
        const val fasterXmlJacksonModule = "2.13.2"
        const val mockitoKotlin = "4.0.0"
        const val springBootStarterTest = "2.6.6"
        const val springDocOpenApi = "1.6.7"
        const val springFramework = "5.3.16"

        // plugins
        const val benManesVersionsPlugin = "0.42.0"
        const val kotlinCompose = "1.0.1-rc2"
        const val springBoot = "2.6.6"
        const val springDependencyManagement = "1.0.11.RELEASE"
        const val springPlugin = "1.6.20"
    }

    object Dependencies {
        const val mockitoKotlin = "org.mockito.kotlin:mockito-kotlin:${Version.mockitoKotlin}"
        const val springBootStarterTest = "org.springframework.boot:spring-boot-starter-test:${
            Version.springBootStarterTest
        }"

        const val springDocOpenApi = "org.springdoc:springdoc-openapi-ui:${Version.springDocOpenApi}"
    }
}
