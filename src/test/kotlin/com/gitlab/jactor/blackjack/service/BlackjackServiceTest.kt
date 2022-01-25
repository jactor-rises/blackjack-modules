package com.gitlab.jactor.blackjack.service

import com.gitlab.jactor.blackjack.consumer.DeckOfCardsConsumer
import com.gitlab.jactor.blackjack.model.DeckOfCards
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean

@SpringBootTest
@DisplayName("A com.gitlab.jactor.blackjack.service.BlackjackService")
internal class BlackjackServiceTest {

    @MockBean
    private lateinit var deckOfCardsConsumerMock: DeckOfCardsConsumer

    @Autowired
    private lateinit var blackjackService: BlackjackService

    @Test
    fun `should start GameOfBlackjack`() {
        val deckOfCards = DeckOfCards()
        whenever(deckOfCardsConsumerMock.fetchCardsForGame()).thenReturn(deckOfCards)

        blackjackService.createNewGame("Tor Egil")
        val gameOfBlackjack = blackjackService.gameOfBlackjack

        assertAll(
            { assertThat(gameOfBlackjack.deckOfCards).`as`("deckOfCards").isEqualTo(deckOfCards) },
            { assertThat(gameOfBlackjack.playerName).`as`("playerName").isEqualTo("Tor Egil") }
        )
    }
}
