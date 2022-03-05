package com.github.jactor.blackjack.consumer

import com.github.jactor.blackjack.model.Card
import com.github.jactor.blackjack.model.CardsRestTemplate
import com.github.jactor.blackjack.model.DeckOfCards
import com.github.jactor.blackjack.model.Face
import com.github.jactor.blackjack.model.Suit

interface DeckOfCardsConsumer {
    fun fetch(): DeckOfCards

    class DefaultDeckOfCardsConsumer(private val cardsRestTemplate: CardsRestTemplate) : DeckOfCardsConsumer {
        override fun fetch(): DeckOfCards {
            val cards = cardsRestTemplate.getCards()
            return DeckOfCards(deck = cards.map { Card(it) } as MutableList<Card>)
        }
    }

    class ConstructShuffledCards : DeckOfCardsConsumer {
        override fun fetch(): DeckOfCards = DeckOfCards(deck = shuffle52cards())

        companion object {
            internal fun shuffle52cards(): MutableList<Card> {
                val clubs = Face.values().map { Card(Suit.CLUBS, it) } as MutableList
                val diamonds = Face.values().map { Card(Suit.DIAMONDS, it) }
                val hearts = Face.values().map { Card(Suit.HEARTS, it) }
                val spades = Face.values().map { Card(Suit.SPADES, it) }

                clubs.addAll(diamonds)
                clubs.addAll(hearts)
                clubs.addAll(spades)

                return clubs.shuffled() as MutableList<Card>
            }
        }
    }
}
