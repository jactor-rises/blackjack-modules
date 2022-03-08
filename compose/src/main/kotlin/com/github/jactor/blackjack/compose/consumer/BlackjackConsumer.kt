package com.github.jactor.blackjack.compose.consumer

import com.github.jactor.blackjack.compose.model.GameAction
import com.github.jactor.blackjack.compose.model.GameOfBlackjack
import com.github.jactor.blackjack.compose.model.GameTypeInternal
import com.github.jactor.blackjack.compose.model.Stay
import com.github.jactor.blackjack.compose.model.TakeCard
import com.github.jactor.blackjack.dto.ActionDto
import com.github.jactor.blackjack.dto.GameOfBlackjackDto
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.web.client.RestTemplate

interface BlackjackConsumer {
    fun play(nick: String, type: GameTypeInternal, gameAction: GameAction? = null): GameOfBlackjack

    class Default(private val restTemplate: RestTemplate) : BlackjackConsumer {

        override fun play(nick: String, type: GameTypeInternal, gameAction: GameAction?): GameOfBlackjack {
            val gameTypeDto = type.asDto()
            val response = restTemplate.exchange(
                "/play/$nick",
                HttpMethod.POST,
                HttpEntity(
                    when (gameAction) {
                        is TakeCard -> ActionDto(type = gameTypeDto, value = gameAction.action.toDto(), gameId = gameAction.gameId)
                        is Stay -> ActionDto(type = gameTypeDto, value = gameAction.action.toDto(), gameId = gameAction.gameId)
                        else -> ActionDto(type = gameTypeDto, value = gameAction?.action?.toDto())
                    }
                ),
                GameOfBlackjackDto::class.java
            )

            return GameOfBlackjack(response.body ?: throw initIllegalState(nick, response.statusCode))
        }

        private fun initIllegalState(nick: String, status: HttpStatus) = IllegalStateException(
            "Kunne ikke spille blackjack med $nick, status: $status"
        )
    }
}
