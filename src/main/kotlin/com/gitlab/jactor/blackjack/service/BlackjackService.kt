package com.gitlab.jactor.blackjack.service

import com.gitlab.jactor.blackjack.consumer.DeckOfCardsConsumer
import com.gitlab.jactor.blackjack.model.GameOfBlackjack
import org.springframework.stereotype.Service

@Service
class BlackjackService(private val deckOfCardsConsumer: DeckOfCardsConsumer) {
    internal lateinit var gameOfBlackjack: GameOfBlackjack

    fun createNewGame(playerName: String) {
        gameOfBlackjack = GameOfBlackjack(deckOfCards = deckOfCardsConsumer.fetchCardsForGame(), playerName = playerName)
    }
}
