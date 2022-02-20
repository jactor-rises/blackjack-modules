package com.gitlab.jactor.blackjack.compose.model

import com.gitlab.jactor.blackjack.compose.dto.StatusDto

data class Status(
    var dealerScore: Int,
    var playerScore: Int,
    var isGameCompleted: Boolean,
    var result: GameStatus?
) {
    constructor(status: StatusDto?) : this(
        dealerScore = status?.dealerScore ?: 0,
        playerScore = status?.playerScore ?: 0,
        isGameCompleted = status?.isGameCompleted ?:false,
        result = if (status != null) GameStatus.valueOf(status.result) else null
    )

    fun fetchResultOfGame() = result ?: throw IllegalStateException("A round of blackjack always has a result!")
}
