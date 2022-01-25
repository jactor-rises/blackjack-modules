package com.gitlab.jactor.blackjack.model

data class DeckOfCards(private val deck: List<Card>, private val mutableDeck: MutableList<Card> = ArrayList(deck)) {
    fun take(noOfCards: Int): MutableList<Card> {
        val slice = mutableDeck.slice(0 until noOfCards)
        mutableDeck.removeAll(slice)

        return ArrayList(slice)
    }
}
