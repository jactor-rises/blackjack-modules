package com.gitlab.jactor.blackjack.model

data class DeckOfCards(internal val deck: MutableList<Card>) {
    fun take(noOfCards: Int): MutableList<Card> {
        val slice = deck.slice(0 until noOfCards)
        deck.removeAll(slice)

        return slice as MutableList<Card>
    }
}
