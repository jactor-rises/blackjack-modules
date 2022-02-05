package com.gitlab.jactor.blackjack.compose.state

import com.gitlab.jactor.blackjack.compose.ApplicationConfiguration
import com.gitlab.jactor.blackjack.compose.model.GameOfBlackjack
import com.gitlab.jactor.blackjack.compose.model.GameType
import com.gitlab.jactor.blackjack.compose.model.PlayerName
import com.gitlab.jactor.blackjack.compose.service.BlackjackService
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class BlackjackState(private val fromContext: CoroutineContext) : Lce<GameOfBlackjack>() {
    private val blackjackService = ApplicationConfiguration.fetchBean(BlackjackService::class.java)

    suspend fun play(type: GameType, playerName: PlayerName): GameOfBlackjack {
        return withContext(fromContext) { blackjackService.play(type, playerName) }
    }
}
