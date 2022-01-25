package com.gitlab.jactor.blackjack.model

object TestUtil {
    fun aDeckOfCardsStartingWith(vararg cards: Card): DeckOfCards = DeckOfCards(cards.toList())
    fun aFullDeckOfCards(): DeckOfCards {
        val clubs = Face.values().map { Card(Suit.CLUBS, it) } as MutableList
        val diamonds = Face.values().map { Card(Suit.DIAMONDS, it) }
        val hearts = Face.values().map { Card(Suit.HEARTS, it) }
        val spades = Face.values().map { Card(Suit.SPADES, it) }

        clubs.addAll(diamonds)
        clubs.addAll(hearts)
        clubs.addAll(spades)

        return DeckOfCards(clubs.shuffled())
    }
}
