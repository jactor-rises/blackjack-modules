package com.github.jactor.blackjack.compose.model

import com.github.jactor.blackjack.compose.state.GameOfBlackjackException
import com.github.jactor.blackjack.dto.GameOfBlackjackDto

data class GameOfBlackjack(
    val dealerHand: List<Card>,
    val nickOfPlayer: String,
    val playerHand: List<Card>,
    val status: Status,
    val gameType: GameTypeInternal,
    val gameId: String
) {
    val playerName: PlayerName get() = name ?: throw IllegalStateException("Player name has not been added!")
    private var name: PlayerName? = null

    constructor(gameOfBlackjackDto: GameOfBlackjackDto) : this(
        dealerHand = gameOfBlackjackDto.dealerHand.map { Card(it) },
        nickOfPlayer = gameOfBlackjackDto.nickOfPlayer,
        playerHand = gameOfBlackjackDto.playerHand.map { Card(it) },
        status = Status(gameOfBlackjackDto.status),
        gameType = GameTypeInternal.valueOf(gameOfBlackjackDto.gameType),
        gameId = gameOfBlackjackDto.gameId
    ) {
        if (gameOfBlackjackDto.error != null) {
            throw GameOfBlackjackException(gameOfBlackjackDto.error!!)
        }
    }

    fun displayWinner() = "${fetchWinner()} ${if (isResultBlackjack()) "fikk blackjack" else "er vinneren"}!"

    private fun fetchWinner() = when (status.fetchResultOfGame()) {
        GameStatus.DEALER_WINS -> "Banken"
        GameStatus.PLAYER_WINS -> playerName.capitalized
    }

    private fun isResultBlackjack() = fetchWinnerScore() == 21

    private fun fetchWinnerScore() = when (status.fetchResultOfGame()) {
        GameStatus.DEALER_WINS -> status.dealerScore
        GameStatus.PLAYER_WINS -> status.playerScore
    }

    fun add(playerName: PlayerName): GameOfBlackjack {
        this.name = playerName
        return this
    }
}
