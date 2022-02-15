package com.gitlab.jactor.blackjack.compose.model

import com.gitlab.jactor.blackjack.compose.dto.GameOfBlackjackDto
import com.gitlab.jactor.blackjack.compose.state.GameOfBlackjackException

data class GameOfBlackjack(
    val dealerHand: List<Card>,
    val nickOfPlayer: String,
    val playerHand: List<Card>,
    val status: Status,
    val gameType: GameType
) {
    constructor(gameOfBlackjackDto: GameOfBlackjackDto) : this(
        dealerHand = gameOfBlackjackDto.dealerHand.map { Card(it) },
        nickOfPlayer = gameOfBlackjackDto.nickOfPlayer,
        playerHand = gameOfBlackjackDto.playerHand.map { Card(it) },
        status = Status(gameOfBlackjackDto.status),
        gameType = GameType.valueOf(gameOfBlackjackDto.gameType ?: throw IllegalStateException("A game must have a GameType"))
    ) {
        if (gameOfBlackjackDto.error != null) {
            throw GameOfBlackjackException(gameOfBlackjackDto.error!!)
        }
    }

    fun displayWinner(playerName: PlayerName) = "Vinneren er " + when (status.fetchResultOfGame()) {
        GameStatus.DEALER_WINS -> "Magnus"
        GameStatus.PLAYER_WINS -> playerName.capitalized
    }
}
