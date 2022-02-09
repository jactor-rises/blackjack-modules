package com.gitlab.jactor.blackjack.compose.state

import com.gitlab.jactor.blackjack.compose.ApplicationConfiguration
import com.gitlab.jactor.blackjack.compose.dto.Action
import com.gitlab.jactor.blackjack.compose.model.ActionInternal
import com.gitlab.jactor.blackjack.compose.model.GameOfBlackjack
import com.gitlab.jactor.blackjack.compose.model.GameType
import com.gitlab.jactor.blackjack.compose.model.PlayerName
import com.gitlab.jactor.blackjack.compose.service.BlackjackService

class BlackjackState {
    private val blackjackService = ApplicationConfiguration.fetchBean(BlackjackService::class.java)

    fun play(gameType: GameType, action: Action?, playerName: PlayerName): GameOfBlackjack {
        if (gameType == GameType.AUTOMATIC) {
            return blackjackService.playAutomatic(playerName)
        }

        return when (action) {
            Action.END -> blackjackService.playManual(playerName, ActionInternal.END)
            Action.HIT -> blackjackService.playManual(playerName, ActionInternal.HIT)
            Action.START -> blackjackService.playManual(playerName, ActionInternal.START)
            else -> throw IllegalArgumentException("Action kan ikke være null ved manuelle spill!")
        }
    }
}
