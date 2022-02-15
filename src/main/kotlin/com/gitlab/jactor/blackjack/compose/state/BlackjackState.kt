package com.gitlab.jactor.blackjack.compose.state

import com.gitlab.jactor.blackjack.compose.dto.Action
import com.gitlab.jactor.blackjack.compose.model.ActionInternal
import com.gitlab.jactor.blackjack.compose.model.GameOfBlackjack
import com.gitlab.jactor.blackjack.compose.model.GameType
import com.gitlab.jactor.blackjack.compose.model.PlayerName
import com.gitlab.jactor.blackjack.compose.service.BlackjackService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainCoroutineDispatcher

class BlackjackState(
    runScope: MainCoroutineDispatcher = Dispatchers.Main,
    private val gameStateConsumer: (Lce<GameOfBlackjack>) -> Unit,
    private val blackjackService: BlackjackService
) : Lce<GameOfBlackjack>(runScope = runScope, ioScope = Dispatchers.IO) {

    fun playAutomatic(playerName: PlayerName) = play(GameType.AUTOMATIC, null, playerName)
    fun playManual(action: Action, playerName: PlayerName) = play(GameType.MANUAL, action, playerName)

    private fun play(gameType: GameType, action: Action?, playerName: PlayerName) {
        if (gameType == GameType.AUTOMATIC) {
            invoke(lceConsumer = gameStateConsumer, run = { blackjackService.playAutomatic(playerName) })
        }

        if (gameType == GameType.MANUAL) {
            when (action) {
                Action.END -> invoke(lceConsumer = gameStateConsumer, run = { blackjackService.playManual(playerName, ActionInternal.END) })
                Action.HIT -> invoke(lceConsumer = gameStateConsumer, run = { blackjackService.playManual(playerName, ActionInternal.HIT) })
                Action.START -> invoke(lceConsumer = gameStateConsumer, run = { blackjackService.playManual(playerName, ActionInternal.START) })
                else -> throw IllegalStateException("A manual game needs an action!")
            }
        }
    }
}
