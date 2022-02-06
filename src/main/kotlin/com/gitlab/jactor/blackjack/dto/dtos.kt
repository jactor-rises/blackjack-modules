package com.gitlab.jactor.blackjack.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Status for spillet 'blackjack'")
data class GameOfBlackjackDto(
    @Schema(description = "Kallenavnet til spilleren") var nickOfPlayer: String,
    @Schema(description = "Spillerens kort") var playerHand: List<CardDto>,
    @Schema(description = "Delers (aka. Magnus') kort") var dealerHand: List<CardDto>,
    @Schema(description = "Status på spillet") var status: StatusDto
)

@Schema(description = "Et spillekort")
data class CardDto(
    @Schema(description = "Sjanger til kort (en av hjerter, spar, ruter eller kløver)") var suit: String = "",
    @Schema(description = "Verdien til kortet") var value: String = ""
)

@Schema(description = "Status for et spill")
data class StatusDto(
    @Schema(description = "Resultatet av omgangen") var result: GameStatus,
    @Schema(description = "Spillerens poengsum") var playerScore: Int,
    @Schema(description = "Delerens (aka. Magnus') poengsum") var dealerScore: Int,
    @Schema(description = "Om spillet er automatisk ferdig") var isGameCompleted: Boolean
)

enum class GameStatus { PLAYER_WINS, DEALER_WINS }

@Schema(description = "Velkommen til spill melding")
data class WelcomeDto(
    @Schema(description = "Melding: Velkommen") var message: String,
    @Schema(description = "Hvordan spille blackjack") var howTo: String
)

@Schema(description = "Verdien til en url")
data class UrlDto(
    @Schema(description = "Verdien til en url") var value: String = ""
)

@Schema(description = "Beskrivelse av en feil som oppstår")
data class ErrorDto(
    @Schema(description = "Feilmelding") var message: String = "",
    @Schema(description = "Hva som rapporterer feilmeldingen") var provider: String = ""
)

@Schema(description = "Handlingen til en spiller av blackjack")
data class ActionDto(
    @Schema(description = "Start et nytt spill, trekk et kort eller avslutt et spill") var value: Action? = null
)

enum class Action {
    @Schema(description = "Start et nytt spill")
    START,

    @Schema(description = "Trekk et kortl")
    HIT,

    @Schema(description = "Avslutt et spill")
    END
}
