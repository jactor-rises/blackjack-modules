package com.github.jactor.blackjack.controller

import com.github.jactor.blackjack.dto.UrlDto
import com.github.jactor.blackjack.model.CardsRestTemplate
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class UrlController(private val cardsRestTemplate: CardsRestTemplate) {

    @Operation(description = "Henter url som blir brukt til å hente kortene for et spill")
    @GetMapping("/url/cards")
    fun getCardsUrl(): ResponseEntity<UrlDto> {
        return ResponseEntity.ok(UrlDto(value = cardsRestTemplate.url))
    }

    @Operation(description = "Setter url som blir brukt til å hente kortene for et spill")
    @PostMapping("/url/cards")
    fun playCardsUrl(@RequestBody urlDto: UrlDto): ResponseEntity<UrlDto> {
        cardsRestTemplate.url = urlDto.value
        return ResponseEntity.ok(UrlDto(value = cardsRestTemplate.url))
    }
}
