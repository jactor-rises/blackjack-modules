package com.gitlab.jactor.blackjack.controller

import com.gitlab.jactor.blackjack.dto.GameOfBlackjackDto
import com.gitlab.jactor.blackjack.dto.WelcomeDto
import com.gitlab.jactor.blackjack.service.GameService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class GameController(private val gameService: GameService) {

    @Operation(description = "Henter en kort melding om hvordan utføre et spill")
    @GetMapping("/")
    fun get(): ResponseEntity<WelcomeDto> {
        return ResponseEntity.ok(
            WelcomeDto(
                message = "Velkommen til en runde med Blackjack",
                howTo = "Gjør en post til endepunkt '/blackjack/play/{kallenavn}'"
            )
        )
    }

    @Operation(description = "Utfører et spill av blackjack for et kallenavn")
    @PostMapping("/play/{nick}")
    fun play(@PathVariable nick: String): ResponseEntity<GameOfBlackjackDto> {
        return ResponseEntity.ok(gameService.createNewGame(nick).completeGame().toDto())
    }
}