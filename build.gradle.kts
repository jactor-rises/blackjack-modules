plugins {
    id("com.github.ben-manes.versions") version BlackjackModules.Version.benManesVersionsPlugin
}

subprojects {
    apply(plugin = "blackjack.kotlin-conventions")
    apply(plugin = "com.github.ben-manes.versions")
}
