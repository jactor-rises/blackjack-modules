package com.gitlab.jactor.blackjack.service

import com.gitlab.jactor.blackjack.consumer.DeckOfCardsConsumer
import com.gitlab.jactor.blackjack.model.GameOfBlackjack
import org.springframework.stereotype.Service

@Service
class BlackjackService(private val deckOfCardsConsumer: DeckOfCardsConsumer) {
    private val gamePerPlayer: MutableMap<String, GameOfBlackjack> = HashMap()

    fun createNewGame(nick: String): GameOfBlackjack {
        if (gamePerPlayer.contains(nick)) {
            throw IllegalArgumentException("Spill for spiller med navn '$nick' er allerede startet!")
        }

        gamePerPlayer[nick] = GameOfBlackjack(deckOfCards = deckOfCardsConsumer.fetch(), nick = nick)

        return gamePerPlayer[nick]!!
    }

    fun forPlayer(nick: String): GameOfBlackjack {
        return gamePerPlayer[nick] ?: throw IllegalArgumentException("Ingen spiller med kallenavnet '$nick'!")
    }
}
