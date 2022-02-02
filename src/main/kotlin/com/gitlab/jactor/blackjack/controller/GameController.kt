package com.gitlab.jactor.blackjack.controller

import com.gitlab.jactor.blackjack.dto.ActionDto
import com.gitlab.jactor.blackjack.dto.GameOfBlackjackDto
import com.gitlab.jactor.blackjack.dto.WelcomeDto
import com.gitlab.jactor.blackjack.model.Action
import com.gitlab.jactor.blackjack.service.GameService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class GameController(private val gameService: GameService) {

    @Operation(description = "Henter en kort melding om hvordan utføre et spill")
    @GetMapping("/")
    fun get(): ResponseEntity<WelcomeDto> {
        return ResponseEntity.ok(
            WelcomeDto(
                message = "Velkommen til en runde med Blackjack",
                howTo = """
                    * Gjør en post til endepunkt '/play/{kallenavn} for å utføre et helautmatisk spill (Ace = 11 poeng)
                    * Gjør en post til endepunkt '/start/{kallenavn} for å starte et spill (Ace er 11 eller 1 poeng)
                      * Videre spill på samme kallenavn er post til endepunkt '/running/{kallenavn}'
                    """.trimIndent()
            )
        )
    }

    @Operation(description = "Utfører et spill av blackjack for et kallenavn")
    @PostMapping("/play/{nick}")
    fun play(@PathVariable nick: String): ResponseEntity<GameOfBlackjackDto> {
        return ResponseEntity.ok(gameService.createNewGame(nick).completeGame().logResult().toDto())
    }

    @Operation(description = "Starter et spill av blackjack for et kallenavn")
    @PostMapping("/start/{nick}")
    fun start(@PathVariable nick: String): ResponseEntity<GameOfBlackjackDto> {
        return ResponseEntity.ok(gameService.startGame(nick).toDto())
    }

    @Operation(description = "Fortsetter et spill av blackjack for et kallenavn")
    @PostMapping("/running/{nick}")
    fun running(@PathVariable nick: String, @RequestBody action: ActionDto): ResponseEntity<GameOfBlackjackDto?> {
        return ResponseEntity.ok(gameService.running(nick, Action(action)).toDto())
    }
}