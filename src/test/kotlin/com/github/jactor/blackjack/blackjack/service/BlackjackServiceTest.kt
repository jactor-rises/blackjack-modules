package com.github.jactor.blackjack.blackjack.service

import com.github.jactor.blackjack.blackjack.consumer.DeckOfCardsConsumer
import com.github.jactor.blackjack.blackjack.model.DeckOfCards
import com.github.jactor.blackjack.blackjack.model.GameOfBlackjack
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean

@SpringBootTest
@DisplayName("A com.github.jactor.blackjack.blackjack.service.BlackjackService")
internal class BlackjackServiceTest {

    @MockBean
    private lateinit var deckOfCardsConsumerMock: DeckOfCardsConsumer

    @Autowired
    private lateinit var blackjackService: BlackjackService

    @Test
    fun `should fetch deck of cards for a new game of blackjack`() {
        val deckOfCards = DeckOfCards()
        whenever(deckOfCardsConsumerMock.fetchCardsForGame()).thenReturn(deckOfCards)

        assertThat(blackjackService.createNewGame("Tor Egil")).`as`("New game is created").isEqualTo(GameOfBlackjack(deckOfCards = deckOfCards))
    }
}
