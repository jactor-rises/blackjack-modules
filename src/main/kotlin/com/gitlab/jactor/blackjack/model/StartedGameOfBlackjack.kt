package com.gitlab.jactor.blackjack.model

import com.gitlab.jactor.blackjack.dto.StartedGameOfBlackjackDto

data class StartedGameOfBlackjack(val playerHand: List<Card>) {
    fun toDto() = StartedGameOfBlackjackDto(playerHand = playerHand.map { it.toDto() })
}
