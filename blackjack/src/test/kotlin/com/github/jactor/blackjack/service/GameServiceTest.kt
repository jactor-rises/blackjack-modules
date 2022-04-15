package com.github.jactor.blackjack.service

import com.github.jactor.blackjack.consumer.DeckOfCardsConsumer
import com.github.jactor.blackjack.model.TestUtil.aFullDeckOfCards
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
@DisplayName("A com.github.jactor.blackjack.service.BlackjackService")
internal class GameServiceTest {

    @MockkBean
    private lateinit var deckOfCardsConsumerMock: DeckOfCardsConsumer

    @Autowired
    private lateinit var gameService: GameService

    @Test
    fun `should start GameOfBlackjack`() {
        val deckOfCards = aFullDeckOfCards()
        every { deckOfCardsConsumerMock.fetch()} returns deckOfCards

        val gameOfBlackjack = gameService.createNewGame("jactor")

        assertAll(
            { assertThat(gameOfBlackjack.deckOfCards).`as`("deckOfCards").isEqualTo(deckOfCards) },
            { assertThat(gameOfBlackjack.nick).`as`("nick").isEqualTo("jactor") }
        )
    }
}
