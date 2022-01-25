package com.gitlab.jactor.blackjack.model

data class GameOfBlackjack(val deckOfCards: DeckOfCards, val playerName: String) {
    internal val playerHand: MutableList<Card> = deckOfCards.take(2)
    internal val dealerHand: MutableList<Card> = deckOfCards.take(2)
}
