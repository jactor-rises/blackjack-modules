package com.github.jactor.blackjack.model

import com.github.jactor.blackjack.consumer.DeckOfCardsConsumer

object TestUtil {
    fun aFullDeckOfCards(): DeckOfCards = DeckOfCards(DeckOfCardsConsumer.ConstructShuffledCards.shuffle52cards())

    fun aDeckOfCardsStartingWith(firstCardsInDeck: List<String>): DeckOfCards {
        val cards = firstCardsInDeck.map { initCard(it) }.toTypedArray()
        return aDeckOfCardsStartingWith(cards = cards)
    }

    fun aDeckOfCardsStartingWith(vararg cards: Card): DeckOfCards {
        val shuffledDeckOfCards = DeckOfCardsConsumer.ConstructShuffledCards.shuffle52cards()
        cards.forEach { shuffledDeckOfCards.remove(it) }

        val deckStartsWith: MutableList<Card> = ArrayList(cards.toList())
        deckStartsWith.addAll(shuffledDeckOfCards)

        return DeckOfCards(deckStartsWith)
    }


    private fun initCard(card: String): Card {
        if (card.length >= 2) {
            return Card(Suit.from(card[0]), Face.from(card.substring(1)))
        }

        throw IllegalArgumentException("$card is of unsupported length, format 'xy' where x is suit and y is card face (at least one character)")
    }
}
