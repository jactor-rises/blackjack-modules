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
internal class BlackjackServiceTest {

    @MockBean
    private lateinit var deckOfCardsConsumerMock: DeckOfCardsConsumer

    @Autowired
    private lateinit var blackjackService: BlackjackService

    @Test
    fun `should start GameOfBlackjack`() {
        val deckOfCards = aFullDeckOfCards()
        whenever(deckOfCardsConsumerMock.fetch()).thenReturn(deckOfCards)

        val gameOfBlackjack = blackjackService.createNewGame("jactor")

        assertAll(
            { assertThat(gameOfBlackjack.deckOfCards).`as`("deckOfCards").isEqualTo(deckOfCards) },
            { assertThat(gameOfBlackjack.nick).`as`("nick").isEqualTo("jactor") }
        )
    }

    @Test
    fun `should prevent a new game for player with a nick who is already playing`() {
        whenever(deckOfCardsConsumerMock.fetch()).thenReturn(aFullDeckOfCards())
        blackjackService.createNewGame("jalla")

        assertThatIllegalArgumentException().isThrownBy { blackjackService.createNewGame("jalla") }
            .withMessage("Spill for spiller med navn 'jalla' er allerede startet!")
    }
}
