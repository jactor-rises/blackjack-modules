package com.gitlab.jactor.blackjack.compose.service

import com.gitlab.jactor.blackjack.compose.consumer.BlackjackConsumer
import com.gitlab.jactor.blackjack.compose.model.ActionInternal
import com.gitlab.jactor.blackjack.compose.model.GameOfBlackjack
import com.gitlab.jactor.blackjack.compose.model.GameType
import com.gitlab.jactor.blackjack.compose.model.PlayerName

interface BlackjackService {
    fun playAutomatic(playerName: PlayerName): GameOfBlackjack
    fun playManual(playerName: PlayerName, actionInternal: ActionInternal): GameOfBlackjack

    class DefaultBlackjackService(private val blackjackConsumer: BlackjackConsumer) : BlackjackService {
        override fun playAutomatic(playerName: PlayerName) = blackjackConsumer.play(nick = playerName.nick, type = GameType.AUTOMATIC)

        override fun playManual(playerName: PlayerName, actionInternal: ActionInternal) = blackjackConsumer.play(
            nick = playerName.nick, type = GameType.MANUAL, actionInternal = actionInternal
        )
    }
}
