group = "com.github.jactor-rises"
description = "blackjack:dto"

dependencies {
    implementation(BlackjackModules.Dependencies.springDocOpenApi)
    testImplementation(BlackjackModules.Dependencies.springBootStarterTest)
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:${BlackjackModules.Version.fasterXmlJacksonModule}")
}
