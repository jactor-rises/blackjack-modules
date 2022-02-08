package com.gitlab.jactor.blackjack.model

data class DeckOfCards(internal val deck: MutableList<Card>) {
    fun takeCard() = deck.removeAt(0)
    fun take(noOfCards: Int): MutableList<Card> {
        val slice = deck.slice(0 until noOfCards)
        deck.removeAll(slice.toSet())

        return slice as MutableList<Card>
    }
}
