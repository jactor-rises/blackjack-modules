package com.github.jactor.blackjack.compose.service

import com.github.jactor.blackjack.compose.consumer.BlackjackConsumer
import com.github.jactor.blackjack.compose.model.ActionInternal
import com.github.jactor.blackjack.compose.model.GameOfBlackjack
import com.github.jactor.blackjack.compose.model.GameType
import com.github.jactor.blackjack.compose.model.PlayerName

interface BlackjackService {
    fun playAutomatic(playerName: PlayerName): GameOfBlackjack
    fun playManual(playerName: PlayerName, actionInternal: ActionInternal): GameOfBlackjack

    class DefaultBlackjackService(private val blackjackConsumer: BlackjackConsumer) : BlackjackService {
        override fun playAutomatic(playerName: PlayerName) = blackjackConsumer.play(nick = playerName.nick, type = GameType.AUTOMATIC)
            .with(playerName)

        override fun playManual(playerName: PlayerName, actionInternal: ActionInternal) = blackjackConsumer.play(
            nick = playerName.nick, type = GameType.MANUAL, actionInternal = actionInternal
        ).with(playerName)
    }
}
