package com.github.jactor.blackjack.compose

import com.github.jactor.blackjack.compose.model.PlayerName

object Constants {
    const val DEFAULT_PLAYER_NAME = "Player One"
    const val WHAT_NAME = "What is your name?"
    val DEFAULT_PLAYER_NICK = PlayerName.asLowerCaseAndDashAsSpace(DEFAULT_PLAYER_NAME)
}