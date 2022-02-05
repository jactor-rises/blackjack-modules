package com.gitlab.jactor.blackjack.compose.model

import com.gitlab.jactor.blackjack.compose.dto.GameOfBlackjackDto

data class GameOfBlackjack(
    val dealerHand: List<Card>,
    val nickOfPlayer: String,
    val playerHand: List<Card>,
    val status: Status
) {
    constructor(gameOfBlackjackDto: GameOfBlackjackDto) : this(
        dealerHand = gameOfBlackjackDto.dealerHand.map { Card(it) },
        nickOfPlayer = gameOfBlackjackDto.nickOfPlayer,
        playerHand = gameOfBlackjackDto.playerHand.map { Card(it) },
        status = Status(gameOfBlackjackDto.status)
    )

    fun displayWinner() = "Vinneren er " + when (status.status) {
        GameStatus.DEALER_WINS -> "Magnus"
        GameStatus.PLAYER_WINS -> nickOfPlayer
        GameStatus.NOT_CONCLUDED -> "ikke bestemt!"
    }
}
