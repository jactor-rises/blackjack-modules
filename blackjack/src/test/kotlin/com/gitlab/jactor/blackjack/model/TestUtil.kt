package com.gitlab.jactor.blackjack.model

object TestUtil {
    fun aFullDeckOfCards(): DeckOfCards = DeckOfCards(shuffleFullDeckOfCards())

    fun aDeckOfCardsStartingWith(firstCardsInDeck: List<String>): DeckOfCards {
        val cards = firstCardsInDeck.map { initCard(it) }.toTypedArray()
        return aDeckOfCardsStartingWith(cards = cards)
    }

    fun aDeckOfCardsStartingWith(vararg cards: Card): DeckOfCards {
        val shuffledDeckOfCards = shuffleFullDeckOfCards()
        cards.forEach { shuffledDeckOfCards.remove(it) }

        val deckStartsWith: MutableList<Card> = ArrayList(cards.toList())
        deckStartsWith.addAll(shuffledDeckOfCards)

        return DeckOfCards(deckStartsWith)
    }

    private fun shuffleFullDeckOfCards(): MutableList<Card> {
        val clubs = Face.values().map { Card(Suit.CLUBS, it) } as MutableList
        val diamonds = Face.values().map { Card(Suit.DIAMONDS, it) }
        val hearts = Face.values().map { Card(Suit.HEARTS, it) }
        val spades = Face.values().map { Card(Suit.SPADES, it) }

        clubs.addAll(diamonds)
        clubs.addAll(hearts)
        clubs.addAll(spades)

        return clubs.shuffled() as MutableList<Card>
    }

    private fun initCard(card: String): Card {
        if (card.length >= 2) {
            return Card(Suit.from(card[0]), Face.from(card.substring(1)))
        }

        throw IllegalArgumentException("$card is of unsupported length, format 'xy' where x is suit and y is card face (at least one character)")
    }
}
