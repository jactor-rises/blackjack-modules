package com.gitlab.jactor.blackjack.service

import com.gitlab.jactor.blackjack.consumer.DeckOfCardsConsumer
import com.gitlab.jactor.blackjack.model.TestUtil.aFullDeckOfCards
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean

@SpringBootTest
@DisplayName("A com.gitlab.jactor.blackjack.service.BlackjackService")
internal class GameServiceTest {

    @MockBean
    private lateinit var deckOfCardsConsumerMock: DeckOfCardsConsumer

    @Autowired
    private lateinit var gameService: GameService

    @Test
    fun `should start GameOfBlackjack`() {
        val deckOfCards = aFullDeckOfCards()
        whenever(deckOfCardsConsumerMock.fetch()).thenReturn(deckOfCards)

        val gameOfBlackjack = gameService.createNewGame("jactor")

        assertAll(
            { assertThat(gameOfBlackjack.deckOfCards).`as`("deckOfCards").isEqualTo(deckOfCards) },
            { assertThat(gameOfBlackjack.nick).`as`("nick").isEqualTo("jactor") }
        )
    }
}
