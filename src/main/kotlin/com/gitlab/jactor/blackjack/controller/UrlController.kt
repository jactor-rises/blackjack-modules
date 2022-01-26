package com.gitlab.jactor.blackjack.controller

import com.gitlab.jactor.blackjack.dto.UrlDto
import com.gitlab.jactor.blackjack.model.CardsUrl
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class UrlController {

    @Operation(description = "Henter url som blir brukt til å hente kortene for et spill")
    @GetMapping("/url/cards")
    fun getCards(): ResponseEntity<UrlDto> {
        return ResponseEntity.ok(UrlDto(value = CardsUrl.url))
    }

    @Operation(description = "Setter url som blir brukt til å hente kortene for et spill")
    @PostMapping("/url/cards")
    fun play(@RequestBody urlDto: UrlDto): ResponseEntity<UrlDto> {
        CardsUrl.url = urlDto.value
        return ResponseEntity.ok(UrlDto(value = CardsUrl.url))
    }
}
