package com.github.jactor.blackjack.compose.consumer

import com.github.jactor.blackjack.compose.dto.ActionDto
import com.github.jactor.blackjack.compose.dto.GameOfBlackjackDto
import com.github.jactor.blackjack.compose.model.ActionInternal
import com.github.jactor.blackjack.compose.model.GameOfBlackjack
import com.github.jactor.blackjack.compose.model.GameType
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.web.client.RestTemplate

interface BlackjackConsumer {
    fun play(nick: String, type: GameType, actionInternal: ActionInternal? = null): GameOfBlackjack

    class DefaultBlackjackConsumer(private val restTemplate: RestTemplate) : BlackjackConsumer {

        override fun play(nick: String, type: GameType, actionInternal: ActionInternal?): GameOfBlackjack {
            val response = restTemplate.exchange(
                "/play/$nick",
                HttpMethod.POST,
                HttpEntity(ActionDto(type = type.asDto(), value = actionInternal?.toDto())),
                GameOfBlackjackDto::class.java
            )

            return GameOfBlackjack(response.body ?: throw initIllegalState(nick, response.statusCode))
        }

        private fun initIllegalState(nick: String, status: HttpStatus) = IllegalStateException(
            "Kunne ikke spille blackjack med $nick, status: $status"
        )
    }
}
