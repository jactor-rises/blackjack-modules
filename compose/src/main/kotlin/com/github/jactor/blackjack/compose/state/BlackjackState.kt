package com.github.jactor.blackjack.compose.state

import com.github.jactor.blackjack.compose.ApplicationConfiguration
import com.github.jactor.blackjack.dto.Action
import com.github.jactor.blackjack.compose.model.ActionInternal
import com.github.jactor.blackjack.compose.model.GameOfBlackjack
import com.github.jactor.blackjack.compose.model.GameTypeInternal
import com.github.jactor.blackjack.compose.model.PlayerName
import com.github.jactor.blackjack.compose.service.BlackjackService
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

    fun playAutomatic() = play(GameTypeInternal.AUTOMATIC, null)
    fun playManual(action: Action) = play(GameTypeInternal.MANUAL, action)

    private fun play(gameTypeInternal: GameTypeInternal, action: Action?) {
        initializeBlackjackService()

        when (gameTypeInternal) {
            GameTypeInternal.AUTOMATIC -> invoke(lceConsumer = gameStateConsumer, run = { blackjackService.playAutomatic(currentPlayerName.invoke()) })
            GameTypeInternal.MANUAL -> when (action) {
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
