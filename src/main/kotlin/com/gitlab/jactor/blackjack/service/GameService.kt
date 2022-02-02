package com.gitlab.jactor.blackjack.service

import com.gitlab.jactor.blackjack.consumer.DeckOfCardsConsumer
import com.gitlab.jactor.blackjack.failure.UnknownPlayerException
import com.gitlab.jactor.blackjack.model.Action
import com.gitlab.jactor.blackjack.model.GameOfBlackjack
import org.springframework.stereotype.Service

@Service
class GameService(private val deckOfCardsConsumer: DeckOfCardsConsumer) {
    private val gameForNick: MutableMap<String, GameOfBlackjack> = HashMap()
    fun createNewGame(nick: String) = GameOfBlackjack(deckOfCards = deckOfCardsConsumer.fetch(), nick = nick)
    fun startGame(nick: String) = gameForNick.computeIfAbsent(nick) { GameOfBlackjack(deckOfCards = deckOfCardsConsumer.fetch(), nick) }

    fun running(nick: String, action: Action): GameOfBlackjack {
        val gameOfBlackjack = gameForNick[nick]
        return gameOfBlackjack?.play(action) ?: throw UnknownPlayerException(nick)
    }
}
