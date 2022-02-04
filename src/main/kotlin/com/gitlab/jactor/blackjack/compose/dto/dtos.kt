package com.gitlab.jactor.blackjack.compose.dto

data class GameOfBlackjackDto(
    var nickOfPlayer: String,
    var playerHand: List<CardDto>,
    var dealerHand: List<CardDto>,
    var status: StatusDto
)

data class CardDto(
    var suit: String = "",
    var value: String = ""
)

data class StatusDto(
    var status: String,
    var playerScore: Int,
    var dealerScore: Int,
    var isGameCompleted: Boolean
)
