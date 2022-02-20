package com.gitlab.jactor.blackjack.consumer

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
@DisplayName("A com.gitlab.jactor.blackjack.consumer.DeckOfCardsConsumer")
internal class DeckOfCardsConsumerTest {

    @Autowired
    private lateinit var deckOfCardsConsumer: DeckOfCardsConsumer

    @Test
    fun `should fetch 52 cards`() {
        val deckOfCards = deckOfCardsConsumer.fetch()

        assertThat(deckOfCards.deck).`as`("deck of cards").hasSize(52)
    }
}
