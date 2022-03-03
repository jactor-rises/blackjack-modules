plugins {
    id("com.github.ben-manes.versions") version Library.Version.benManesVersionsPlugin
}

subprojects {
    apply(plugin = "blackjack.kotlin-conventions")
    apply(plugin = "com.github.ben-manes.versions")
}
