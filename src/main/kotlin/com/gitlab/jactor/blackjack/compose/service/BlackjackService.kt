package com.gitlab.jactor.blackjack.compose.service

import com.gitlab.jactor.blackjack.compose.consumer.BlackjackConsumer
import com.gitlab.jactor.blackjack.compose.model.GameOfBlackjack
import com.gitlab.jactor.blackjack.compose.model.GameType
import com.gitlab.jactor.blackjack.compose.model.PlayerName

interface BlackjackService {
    fun play(type: GameType, playerName: PlayerName): GameOfBlackjack

    class DefaultBlackjackService(private val blackjackConsumer: BlackjackConsumer) : BlackjackService {
        override fun play(type: GameType, playerName: PlayerName): GameOfBlackjack {
            return when (type) {
                GameType.AUTOMATIC -> blackjackConsumer.play(playerName.nick)
                GameType.MANUAL -> blackjackConsumer.playManual(playerName.nick)
            }
        }
    }
}
