package com.github.jactor.blackjack.compose.model

data class PlayerName(private val name: String) {
    val capitalized: String
        get() = name.split(" ").toList().joinToString(separator = " ") { string ->
            string.replaceFirstChar { if (it.isLowerCase()) it.titlecase(java.util.Locale.getDefault()) else it.toString() }
        }

    val nick: String get() = name.replace(" ", "-").lowercase()
}
