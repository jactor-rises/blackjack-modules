package com.github.jactor.blackjack.compose.model

import com.github.jactor.blackjack.compose.dto.GameOfBlackjackDto
import com.github.jactor.blackjack.compose.state.GameOfBlackjackException

data class GameOfBlackjack(
    val dealerHand: List<Card>,
    val nickOfPlayer: String,
    val playerHand: List<Card>,
    val status: Status,
    val gameType: GameType
) {
    val playerName: PlayerName get() = name ?: throw IllegalStateException("Player name has not been added!")
    private var name: PlayerName? = null

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

    fun displayWinner() = "Vinneren er " + when (status.fetchResultOfGame()) {
        GameStatus.DEALER_WINS -> "Magnus"
        GameStatus.PLAYER_WINS -> playerName.capitalized
    }

    fun with(playerName: PlayerName): GameOfBlackjack {
        if (playerName.nick != nickOfPlayer) {
            throw IllegalStateException("The nick of added player do not match the game nick ($nickOfPlayer)!")
        }

        this.name = playerName
        return this
    }
}
