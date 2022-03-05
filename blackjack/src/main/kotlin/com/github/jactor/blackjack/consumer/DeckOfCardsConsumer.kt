package com.github.jactor.blackjack.consumer

import com.github.jactor.blackjack.model.Card
import com.github.jactor.blackjack.model.CardsRestTemplate
import com.github.jactor.blackjack.model.DeckOfCards
import org.springframework.stereotype.Component

@Component
class DeckOfCardsConsumer(private val cardsRestTemplate: CardsRestTemplate) {
    fun fetch(): DeckOfCards {
        val cards = cardsRestTemplate.getCards()
        return DeckOfCards(deck = cards.map { Card(it) } as MutableList<Card>)
    }
}
