package com.gitlab.jactor.blackjack.compose.service

import com.gitlab.jactor.blackjack.compose.consumer.BlackjackConsumer
import com.gitlab.jactor.blackjack.compose.model.Action
import com.gitlab.jactor.blackjack.compose.model.GameOfBlackjack
import com.gitlab.jactor.blackjack.compose.model.PlayerName

interface BlackjackService {
    fun playAutomatic(playerName: PlayerName): GameOfBlackjack
    fun playManual(playerName: PlayerName, action: Action): GameOfBlackjack

    class DefaultBlackjackService(private val blackjackConsumer: BlackjackConsumer) : BlackjackService {
        override fun playAutomatic(playerName: PlayerName) = blackjackConsumer.playAutomatic(playerName.nick)
        override fun playManual(playerName: PlayerName, action: Action) = blackjackConsumer.playManual(playerName.nick, action)
    }
}
