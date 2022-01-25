package com.gitlab.jactor.blackjack.service

import com.gitlab.jactor.blackjack.consumer.DeckOfCardsConsumer
import com.gitlab.jactor.blackjack.model.GameOfBlackjack
import org.springframework.stereotype.Service

@Service
class BlackjackService(private val deckOfCardsConsumer: DeckOfCardsConsumer) {
    fun createNewGame(playerName: String): GameOfBlackjack {
        return GameOfBlackjack(deckOfCards = deckOfCardsConsumer.fetchCardsForGame())
    }
}
