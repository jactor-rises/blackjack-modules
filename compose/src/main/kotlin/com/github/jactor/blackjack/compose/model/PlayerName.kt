package com.github.jactor.blackjack.compose.model

import com.github.jactor.blackjack.compose.Constants

data class PlayerName(private val name: String = Constants.DEFAULT_PLAYER_NAME, val nick: String = Constants.DEFAULT_PLAYER_NICK) {
    val capitalized: String
        get() = name.split(" ").toList().joinToString(separator = " ") { string ->
            string.replaceFirstChar { if (it.isLowerCase()) it.titlecase(java.util.Locale.getDefault()) else it.toString() }
        }

    companion object {
        fun asLowerCaseAndDashAsSpace(name: String): String = name.replace(" ", "-").lowercase()
    }
}
