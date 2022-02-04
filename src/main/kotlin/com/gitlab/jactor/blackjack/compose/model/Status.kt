package com.gitlab.jactor.blackjack.compose.model

import com.gitlab.jactor.blackjack.compose.dto.StatusDto

data class Status(
    var dealerScore: Int,
    var playerScore: Int,
    var isGameCompleted: Boolean,
    var status: GameStatus
) {
    constructor(status: StatusDto) : this(
        dealerScore = status.dealerScore,
        playerScore = status.playerScore,
        isGameCompleted = status.isGameCompleted,
        status = GameStatus.valueOf(status.status)
    )
}
