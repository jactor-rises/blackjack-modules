package com.gitlab.jactor.blackjack.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Status for spillet 'blackjack'")
data class GameOfBlackjackDto(
    @Schema(description = "Kallenavnet til spilleren") var nickOfPlayer: String,
    @Schema(description = "Spillerens kort") var playerHand: List<CardDto>,
    @Schema(description = "Delers (aka. Magnus') kort") var dealerHand: List<CardDto>,
    @Schema(description = "Resultatet av spillet") var resultat: ResultDto
)

@Schema(description = "Et spillekort")
data class CardDto(
    @Schema(description = "Sjanger til kort (en av hjerter, spar, ruter eller kløver)") var suit: String = "",
    @Schema(description = "Verdien til kortet") var value: String = ""
)

@Schema(description = "Resultatet av et spill")
data class ResultDto(
    @Schema(description = "Vinneren av spillet") var winner: String,
    @Schema(description = "Spillerens poengsum") var playerScore: Int,
    @Schema(description = "Delers (aka. Magnus') poengsum") var dealerScore: Int

)

@Schema(description = "Velkommen til spill melding")
data class WelcomeDto(
    @Schema(description = "Melding: Velkommen") var message: String,
    @Schema(description = "Hvordan spille blackjack") var howTo: String
)
