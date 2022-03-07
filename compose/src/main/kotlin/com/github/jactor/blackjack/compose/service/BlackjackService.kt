package com.github.jactor.blackjack.compose.service

import com.github.jactor.blackjack.compose.consumer.BlackjackConsumer
import com.github.jactor.blackjack.compose.model.GameAction
import com.github.jactor.blackjack.compose.model.GameOfBlackjack
import com.github.jactor.blackjack.compose.model.GameTypeInternal
import com.github.jactor.blackjack.compose.model.PlayerName

interface BlackjackService {
    fun playAutomatic(playerName: PlayerName): GameOfBlackjack
    fun playManual(playerName: PlayerName, gameAction: GameAction?): GameOfBlackjack

    class DefaultBlackjackService(private val blackjackConsumer: BlackjackConsumer) : BlackjackService {
        override fun playAutomatic(playerName: PlayerName): GameOfBlackjack {
            return blackjackConsumer.play(nick = playerName.nick, type = GameTypeInternal.AUTOMATIC).add(playerName)
        }

        override fun playManual(playerName: PlayerName, gameAction: GameAction?): GameOfBlackjack {
            return blackjackConsumer.play(nick = playerName.nick, type = GameTypeInternal.MANUAL, gameAction = gameAction).add(playerName)
        }
    }
}
