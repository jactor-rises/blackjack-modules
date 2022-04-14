group = "com.github.jactor-rises"
description = "blackjack:dto"

dependencies {
    implementation(BlackjackModules.Dependencies.springDocOpenApi)
    testImplementation("org.springframework.boot:spring-boot-starter-test:2.6.4")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:${BlackjackModules.Version.fasterXmlJacksonModule}")
}
