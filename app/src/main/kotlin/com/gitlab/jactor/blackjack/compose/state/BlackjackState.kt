package com.gitlab.jactor.blackjack.compose.state

import com.gitlab.jactor.blackjack.compose.ApplicationConfiguration
import com.gitlab.jactor.blackjack.compose.dto.Action
import com.gitlab.jactor.blackjack.compose.model.ActionInternal
import com.gitlab.jactor.blackjack.compose.model.GameOfBlackjack
import com.gitlab.jactor.blackjack.compose.model.GameType
import com.gitlab.jactor.blackjack.compose.model.PlayerName
import com.gitlab.jactor.blackjack.compose.service.BlackjackService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainCoroutineDispatcher

class BlackjackState(
    coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO,
    val runScope: MainCoroutineDispatcher = Dispatchers.Main,
    val currentPlayerName: () -> PlayerName
) : Lce<GameOfBlackjack>(runScope = runScope, coroutineDispatcher = coroutineDispatcher) {
    object NotStartet : Lce<GameOfBlackjack>()

    lateinit var gameStateConsumer: (Lce<GameOfBlackjack>) -> Unit
    private lateinit var blackjackService: BlackjackService

    fun playAutomatic() = play(GameType.AUTOMATIC, null)
    fun playManual(action: Action) = play(GameType.MANUAL, action)

    private fun play(gameType: GameType, action: Action?) {
        initializeBlackjackService()

        when (gameType) {
            GameType.AUTOMATIC -> invoke(lceConsumer = gameStateConsumer, run = { blackjackService.playAutomatic(currentPlayerName.invoke()) })
            GameType.MANUAL -> when (action) {
                Action.END -> invoke(
                    lceConsumer = gameStateConsumer,
                    run = { blackjackService.playManual(currentPlayerName.invoke(), ActionInternal.END) })
                Action.HIT -> invoke(
                    lceConsumer = gameStateConsumer,
                    run = { blackjackService.playManual(currentPlayerName.invoke(), ActionInternal.HIT) }
                )
                Action.START -> invoke(
                    lceConsumer = gameStateConsumer,
                    run = { blackjackService.playManual(currentPlayerName.invoke(), ActionInternal.START) }
                )

                null -> throw IllegalStateException("A manual game needs an action!")
            }
        }
    }

    private fun initializeBlackjackService() {
        if (!(this::blackjackService.isInitialized)) {
            setBlackjackService(ApplicationConfiguration.fetchBean(BlackjackService::class.java))
        }
    }

    internal fun setBlackjackService(blackjackService: BlackjackService) {
        this.blackjackService = blackjackService
    }
}
