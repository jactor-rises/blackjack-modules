package com.gitlab.jactor.blackjack.compose.state

import com.gitlab.jactor.blackjack.compose.ApplicationConfiguration
import com.gitlab.jactor.blackjack.compose.model.Action
import com.gitlab.jactor.blackjack.compose.model.GameOfBlackjack
import com.gitlab.jactor.blackjack.compose.model.GameType
import com.gitlab.jactor.blackjack.compose.model.PlayerName
import com.gitlab.jactor.blackjack.compose.service.BlackjackService
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class BlackjackState(private val fromContext: CoroutineContext) : Lce<GameOfBlackjack>() {
    private val blackjackService = ApplicationConfiguration.fetchBean(BlackjackService::class.java)

    suspend fun play(type: GameType, playerName: PlayerName): GameOfBlackjack {
        return when(type) {
            GameType.AUTOMATIC -> withContext(fromContext) { blackjackService.playAutomatic(playerName) }
            GameType.MANUAL -> withContext(fromContext) { blackjackService.playManual(playerName, Action.START) }
        }
    }
}
