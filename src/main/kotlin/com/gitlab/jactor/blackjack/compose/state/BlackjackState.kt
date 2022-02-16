package com.gitlab.jactor.blackjack.compose.state

import com.gitlab.jactor.blackjack.compose.ApplicationConfiguration
import com.gitlab.jactor.blackjack.compose.dto.Action
import com.gitlab.jactor.blackjack.compose.model.ActionInternal
import com.gitlab.jactor.blackjack.compose.model.GameOfBlackjack
import com.gitlab.jactor.blackjack.compose.model.GameType
import com.gitlab.jactor.blackjack.compose.model.PlayerName
import com.gitlab.jactor.blackjack.compose.service.BlackjackService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainCoroutineDispatcher

class BlackjackState(
    val runScope: MainCoroutineDispatcher = Dispatchers.Main,
    val playerName: PlayerName
) : Lce<GameOfBlackjack>(runScope = runScope, coroutineScope = Dispatchers.IO) {
    object NotStartet : Lce<GameOfBlackjack>()

    lateinit var gameStateConsumer: (Lce<GameOfBlackjack>) -> Unit
    private var blackjackService: BlackjackService? = null

    fun playAutomatic() = play(GameType.AUTOMATIC, null)
    fun playManual(action: Action) = play(GameType.MANUAL, action)

    private fun play(gameType: GameType, action: Action?) {
        if (gameType == GameType.AUTOMATIC) {
            invoke(lceConsumer = gameStateConsumer, run = { fetchBlackjackService().playAutomatic(playerName) })
        }

        if (gameType == GameType.MANUAL) {
            when (action) {
                Action.END -> invoke(lceConsumer = gameStateConsumer, run = { fetchBlackjackService().playManual(playerName, ActionInternal.END) })
                Action.HIT -> invoke(lceConsumer = gameStateConsumer, run = { fetchBlackjackService().playManual(playerName, ActionInternal.HIT) })
                Action.START -> invoke(
                    lceConsumer = gameStateConsumer,
                    run = { fetchBlackjackService().playManual(playerName, ActionInternal.START) }
                )

                else -> throw IllegalStateException("A manual game needs an action!")
            }
        }
    }

    private fun fetchBlackjackService(): BlackjackService {
        if (blackjackService != null) {
            return blackjackService!!
        }

        blackjackService = ApplicationConfiguration.fetchBean(BlackjackService::class.java)

        return blackjackService!!
    }

    internal fun setBlackjackService(blackjackService: BlackjackService) {
        this.blackjackService = blackjackService
    }
}
