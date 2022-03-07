package com.github.jactor.blackjack.compose.model

interface GameAction {
    val action: ActionInternal
}

data class StartManualGame(override val action: ActionInternal = ActionInternal.START): GameAction
data class Stay(val gameId: String, override val action: ActionInternal = ActionInternal.END): GameAction
data class TakeCard(val gameId: String, override val action: ActionInternal = ActionInternal.HIT): GameAction
