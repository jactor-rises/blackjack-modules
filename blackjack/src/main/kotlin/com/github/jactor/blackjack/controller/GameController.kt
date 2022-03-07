package com.github.jactor.blackjack.controller

import com.github.jactor.blackjack.dto.ActionDto
import com.github.jactor.blackjack.dto.GameOfBlackjackDto
import com.github.jactor.blackjack.dto.GameType
import com.github.jactor.blackjack.dto.WelcomeDto
import com.github.jactor.blackjack.failure.UnknownGameException
import com.github.jactor.blackjack.model.Action
import com.github.jactor.blackjack.model.GameId
import com.github.jactor.blackjack.service.GameService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class GameController(private val gameService: GameService) {

    @Operation(description = "Henter en kort melding om hvordan utføre et spill")
    @ApiResponse(responseCode = "200", description = "Velkommen til blackjack med hvordan beskrivelse")
    @GetMapping("/")
    fun get(): ResponseEntity<WelcomeDto> {
        return ResponseEntity.ok(
            WelcomeDto(
                message = "Velkommen til en runde med Blackjack",
                howTo = """
                    * Gjør en post til endepunkt '/play/{kallenavn}' og med GameType.AUTOMATIC for å utføre et helautmatisk spill (Ace = 11 poeng)
                    * Gjør en post til endepunkt '/play/{kallenavn}' og med GameType.MANUAL samt Action.START for å starte et spill (Ace er 11 eller 1 poeng)
                      * Videre spill på samme kallenavn ved post til endepunkt '/play/{kallenavn}' og med GameType.MANUAL samt en Action
                        * Action må inneholde hva som skal utføres (HIT eller END), samt gameId til spillet som det skal utføres på
                    """.trimIndent()
            )
        )
    }

    @Operation(description = "Utfører et spill av blackjack for et kallenavn")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "Status på et spill"),
        ApiResponse(
            responseCode = "400",
            description = "Når spillet er pågående (GameType == MANUAL && Action != START) men det ikke finnes et spill for navnet. (Se ErrorDto)"
        )
    )
    @PostMapping("/play/{nick}")
    fun play(@PathVariable nick: String, @RequestBody action: ActionDto): ResponseEntity<GameOfBlackjackDto> {
        return when (action.type) {
            GameType.AUTOMATIC -> ResponseEntity.ok(gameService.playAutomaticGame(nick).toDto())
            GameType.MANUAL -> {
                when (Action.valueOf(action)) {
                    Action.START -> ResponseEntity.ok(gameService.startGame(nick).toDto())
                    Action.HIT -> ResponseEntity.ok(gameService.takeCard(fetchGameId(action.gameId)).toDto())
                    Action.END -> ResponseEntity.ok(gameService.stop(fetchGameId(action.gameId)).toDto())
                }
            }
        }
    }

    private fun fetchGameId(gameId: String?): GameId {
        if (gameId == null) {
            throw UnknownGameException()
        }

        return GameId(gameId)
    }
}
