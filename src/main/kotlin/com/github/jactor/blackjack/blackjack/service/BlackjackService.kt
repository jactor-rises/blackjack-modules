package com.github.jactor.blackjack.blackjack.service

import com.github.jactor.blackjack.blackjack.consumer.DeckOfCardsConsumer
import com.github.jactor.blackjack.blackjack.model.GameOfBlackjack
import org.springframework.stereotype.Service

@Service
class BlackjackService(private val deckOfCardsConsumer: DeckOfCardsConsumer) {
    fun createNewGame(playerName: String): GameOfBlackjack {
        return GameOfBlackjack(deckOfCards = deckOfCardsConsumer.fetchCardsForGame())
    }
}
