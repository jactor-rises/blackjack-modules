package com.gitlab.jactor.blackjack.compose.consumer

import com.gitlab.jactor.blackjack.compose.dto.GameOfBlackjackDto
import com.gitlab.jactor.blackjack.compose.model.GameOfBlackjack
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.web.client.RestTemplate

class BlackjackConsumer(private val restTemplate: RestTemplate) {
    fun play(nick: String): GameOfBlackjack {
        val response = restTemplate.exchange("/play/$nick", HttpMethod.POST, null, GameOfBlackjackDto::class.java)
        return GameOfBlackjack(response.body ?: throw illegalState(nick, response.statusCode))
    }

    private fun illegalState(nick: String, status: HttpStatus) = IllegalStateException("Kunne ikke spille blackjack med $nick, status: ${status}")
}
