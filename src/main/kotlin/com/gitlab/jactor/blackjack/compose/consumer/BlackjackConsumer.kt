package com.gitlab.jactor.blackjack.compose.consumer

import com.gitlab.jactor.blackjack.compose.dto.ActionDto
import com.gitlab.jactor.blackjack.compose.dto.GameOfBlackjackDto
import com.gitlab.jactor.blackjack.compose.model.ActionInternal
import com.gitlab.jactor.blackjack.compose.model.GameOfBlackjack
import com.gitlab.jactor.blackjack.compose.model.GameType
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.web.client.RestTemplate

class BlackjackConsumer(private val restTemplate: RestTemplate) {
    fun playAutomatic(nick: String) = play(nick = nick, type = GameType.AUTOMATIC, actionInternal = null)
    fun playManual(nick: String, actionInternal: ActionInternal) = play(nick = nick, type = GameType.MANUAL, actionInternal = actionInternal)

    private fun play(nick: String, type: GameType, actionInternal: ActionInternal?): GameOfBlackjack {
        val response = restTemplate.exchange(
            "/play/$nick",
            HttpMethod.POST,
            HttpEntity( ActionDto(type = type.asDto(), value = actionInternal?.toDto())),
            GameOfBlackjackDto::class.java
        )

        return GameOfBlackjack(response.body ?: throw illegalState(nick, response.statusCode))
    }

    private fun illegalState(nick: String, status: HttpStatus) = IllegalStateException("Kunne ikke spille blackjack med $nick, status: $status")
}
