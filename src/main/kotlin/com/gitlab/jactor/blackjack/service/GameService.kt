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
    fun playAutomaticGame(nick: String) = createNewGame(nick).completeGame().logResult()
    fun startGame(nick: String): GameOfBlackjack {
        val gameOfBlackjack = GameOfBlackjack(deckOfCards = deckOfCardsConsumer.fetch(), nick = nick, isManualGame = true)

        if (gameOfBlackjack.isNotGameCompleted()) {
            gameForNick[nick] = gameOfBlackjack
        }

        return gameOfBlackjack
    }

    fun running(nick: String, action: Action?): GameOfBlackjack {
        val gameOfBlackjack = gameForNick[nick]?.play(action = action) ?: throw UnknownPlayerException(nick)

        if (gameOfBlackjack.isGameCompleted()) {
            gameForNick.remove(nick)
        }

        return gameOfBlackjack
    }

    fun stop(nick: String): GameOfBlackjack {
        val gameOfBlackjack = gameForNick[nick] ?: throw UnknownPlayerException(nick)
        gameForNick.remove(nick)
        return gameOfBlackjack.completeGame()
    }
}
