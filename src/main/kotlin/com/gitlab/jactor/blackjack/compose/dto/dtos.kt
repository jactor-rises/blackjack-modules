package com.gitlab.jactor.blackjack.compose.dto

data class GameOfBlackjackDto(
    var nickOfPlayer: String = "",
    var playerHand: List<CardDto> = emptyList(),
    var dealerHand: List<CardDto> = emptyList(),
    var status: StatusDto? = null,
    var error: String? = null,
    var gameType: GameTypeDto? = null
)

data class CardDto(
    var suit: String = "",
    var value: String = ""
)

data class StatusDto(
    var result: String = "na",
    var playerScore: Int = 0,
    var dealerScore: Int = 0,
    var isGameCompleted: Boolean = false
)

data class ActionDto(
    var type: GameTypeDto = GameTypeDto.AUTOMATIC,
    var value: Action? = null
)

@Suppress("unused") // used in lambda
enum class GameTypeDto { AUTOMATIC, MANUAL }
@Suppress("unused") // used in lambda
enum class Action { START, HIT, END }
