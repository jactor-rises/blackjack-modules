package com.github.jactor.blackjack.service

import com.github.jactor.blackjack.consumer.DeckOfCardsConsumer
import com.github.jactor.blackjack.failure.UnknownGameException
import com.github.jactor.blackjack.model.Action
import com.github.jactor.blackjack.model.GameId
import com.github.jactor.blackjack.model.GameOfBlackjack
import org.springframework.stereotype.Service

@Service
class GameService(private val deckOfCardsConsumer: DeckOfCardsConsumer) {
    private val gameForId: MutableMap<GameId, GameOfBlackjack> = HashMap()

    internal fun createNewGame(nick: String) = GameOfBlackjack(deckOfCards = deckOfCardsConsumer.fetch(), nick = nick)
    fun playAutomaticGame(nick: String) = createNewGame(nick).completeGame()
    fun startGame(nick: String): GameOfBlackjack {
        val gameOfBlackjack = GameOfBlackjack(deckOfCards = deckOfCardsConsumer.fetch(), nick = nick, isManualGame = true)

        if (gameOfBlackjack.isNotGameCompleted()) {
            gameForId[gameOfBlackjack.gameId] = gameOfBlackjack
        }

        return play(gameOfBlackjack, Action.START)
    }

    fun takeCard(gameId: GameId): GameOfBlackjack {
        val gameOfBlackjack = gameForId[gameId] ?: throw UnknownGameException(gameId)
        return play(gameOfBlackjack, Action.HIT)
    }

    fun stop(gameId: GameId): GameOfBlackjack {
        val gameOfBlackjack = gameForId[gameId] ?: throw UnknownGameException(gameId)
        return play(gameOfBlackjack, Action.END)
    }

    private fun play(gameOfBlackjack: GameOfBlackjack, action: Action): GameOfBlackjack {
        val playedGameOfBlackjack = when (action) {
            Action.START -> gameOfBlackjack
            Action.HIT -> gameOfBlackjack.play(Action.HIT)
            Action.END -> gameOfBlackjack.play(Action.END)
        }

        if (gameOfBlackjack.isGameCompleted() && gameOfBlackjack.withGameId()) {
            gameForId.remove(gameOfBlackjack.gameId)
        }

        return playedGameOfBlackjack
    }
}
