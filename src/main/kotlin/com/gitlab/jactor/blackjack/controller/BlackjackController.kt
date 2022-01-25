package com.gitlab.jactor.blackjack.controller

import com.gitlab.jactor.blackjack.dto.GameOfBlackjackDto
import com.gitlab.jactor.blackjack.service.BlackjackService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class BlackjackController(private val blackjackService: BlackjackService) {

    @GetMapping("/")
    fun get(): ResponseEntity<String> {
        return ResponseEntity.ok(
            """{"msg":"Velkommen til en runde med Blackjack","how","Gjør en post til endepunkt med sti 'blackjack/ny/{kallenavn}'"}"""
        )
    }

    @Operation(description = "Starter et nytt spill av blackjack for et kallenavn")
    @PostMapping("/ny/{nick}")
    fun new(@PathVariable nick: String): ResponseEntity<GameOfBlackjackDto> {
        return ResponseEntity.ok(blackjackService.createNewGame(nick).completeGame().toDto())
    }
}