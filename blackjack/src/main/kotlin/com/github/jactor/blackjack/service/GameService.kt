package com.github.jactor.blackjack.service

import com.github.jactor.blackjack.consumer.DeckOfCardsConsumer
import com.github.jactor.blackjack.failure.UnknownPlayerException
import com.github.jactor.blackjack.model.Action
import com.github.jactor.blackjack.model.GameOfBlackjack
import org.springframework.stereotype.Service

@Service
class GameService(private val deckOfCardsConsumer: DeckOfCardsConsumer) {
    private val gameForNick: MutableMap<String, GameOfBlackjack> = HashMap()

    internal fun createNewGame(nick: String) = GameOfBlackjack(deckOfCards = deckOfCardsConsumer.fetch(), nick = nick)
    fun playAutomaticGame(nick: String) = createNewGame(nick).completeGame()
    fun startGame(nick: String): GameOfBlackjack {
        val gameOfBlackjack = GameOfBlackjack(deckOfCards = deckOfCardsConsumer.fetch(), nick = nick, isManualGame = true)

        if (gameOfBlackjack.isNotGameCompleted()) {
            gameForNick[nick] = gameOfBlackjack
        }

        return play(gameOfBlackjack, Action.START)
    }

    fun takeCard(nick: String): GameOfBlackjack {
        val gameOfBlackjack = gameForNick[nick] ?: throw UnknownPlayerException(nick)
        return play(gameOfBlackjack, Action.HIT)
    }

    fun stop(nick: String): GameOfBlackjack {
        val gameOfBlackjack = gameForNick[nick] ?: throw UnknownPlayerException(nick)
        return play(gameOfBlackjack, Action.END)
    }

    private fun play(gameOfBlackjack: GameOfBlackjack, action: Action): GameOfBlackjack {
        val playedGameOfBlackjack = when (action) {
            Action.START -> gameOfBlackjack
            Action.HIT -> gameOfBlackjack.play(Action.HIT)
            Action.END -> gameOfBlackjack.play(Action.END)
        }

        if (gameOfBlackjack.isGameCompleted()) {
            gameForNick.remove(gameOfBlackjack.nick)
        }

        return playedGameOfBlackjack
    }
}
