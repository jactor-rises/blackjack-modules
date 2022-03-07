package com.github.jactor.blackjack.failure

import com.github.jactor.blackjack.model.GameId

class UnknownGameException(gameId: GameId? = null) : RuntimeException(initUnknownGameMessage(gameId)) {
    companion object {
        private fun initUnknownGameMessage(gameId: GameId?) = "Ingen aktive spill for id $gameId"
    }
}
