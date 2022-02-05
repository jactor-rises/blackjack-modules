package com.gitlab.jactor.blackjack.compose.model

data class PlayerName(val name: String) {
    val nick: String get() = name.replace(" ", "-").lowercase()
}
