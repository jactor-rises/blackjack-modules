package com.github.jactor.blackjack.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Status for spillet 'blackjack'")
data class GameOfBlackjackDto(
    @Schema(description = "Kallenavnet til spilleren") var nickOfPlayer: String = "",
    @Schema(description = "Spillerens kort") var playerHand: List<CardDto> = emptyList(),
    @Schema(description = "Delers (aka. Banken') kort") var dealerHand: List<CardDto> = emptyList(),
    @Schema(description = "Status på spillet") var status: StatusDto? = null,
    @Schema(description = "En feil oppstod under spilling") val error: ErrorDto? = null,
    @Schema(description = "Er dette et automatisk eller manuelt spill") val gameType: GameType = GameType.AUTOMATIC,
    @Schema(description = "Identifiserer et pågående spill") var gameId: String = ""
)

@Schema(description = "Et spillekort")
data class CardDto(
    @Schema(description = "Type kort (en av hjerter, spar, ruter eller kløver)") var suit: String = "",
    @Schema(description = "Verdien til kortet") var value: String = ""
)

@Schema(description = "Status for et spill")
data class StatusDto(
    @Schema(description = "Resultatet av omgangen") var result: GameStatus,
    @Schema(description = "Spillerens poengsum") var playerScore: Int,
    @Schema(description = "Delerens (aka. Banken') poengsum") var dealerScore: Int,
    @Schema(description = "Om spillet er ferdig") var isGameCompleted: Boolean
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
    @Schema(description = "Om det er et automatisk eller et manuelt spill") var type: GameType = GameType.AUTOMATIC,
    @Schema(description = "Start et nytt spill, trekk et kort eller avslutt et spill") var value: Action? = null,
    @Schema(description = "Identifiserer et pågående spill") var gameId: String? = null
)

enum class GameType { AUTOMATIC, MANUAL }

enum class Action {
    @Schema(description = "Start et nytt spill")
    START,

    @Schema(description = "Trekk et kort")
    HIT,

    @Schema(description = "Avslutt et spill")
    END
}
