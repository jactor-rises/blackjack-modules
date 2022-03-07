package com.github.jactor.blackjack.compose.state

import com.github.jactor.blackjack.compose.consumer.BlackjackConsumer
import com.github.jactor.blackjack.compose.model.GameAction
import com.github.jactor.blackjack.compose.model.GameOfBlackjack
import com.github.jactor.blackjack.compose.model.GameTypeInternal
import com.github.jactor.blackjack.compose.model.PlayerName
import com.github.jactor.blackjack.compose.model.StartManualGame
import com.github.jactor.blackjack.compose.service.BlackjackService
import com.github.jactor.blackjack.dto.ErrorDto
import com.github.jactor.blackjack.dto.GameOfBlackjackDto
import com.github.jactor.blackjack.dto.GameType
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll

@DisplayName("A com.github.jactor.blackjack.compose.state.BlackjackState")
internal class BlackjackStateTest {

    @Test
    fun `should state a successful gameplay with content of GameOfBlackjack`() = runBlocking {
        var gameState: Lce<GameOfBlackjack>? = null
        val blackjackState = BlackjackState { PlayerName("junit") }
        blackjackState.gameStateConsumer = { newGameState: Lce<GameOfBlackjack> -> gameState = newGameState }
        blackjackState.setBlackjackService(BlackjackService.DefaultBlackjackService(blackjackConsumer = object : BlackjackConsumer {
            override fun play(nick: String, type: GameTypeInternal, gameAction: GameAction?): GameOfBlackjack = GameOfBlackjack(
                GameOfBlackjackDto(nickOfPlayer = "junit", gameType = GameType.MANUAL)
            )
        }))


        blackjackState.play(StartManualGame())

        delay(timeMillis = 500) // to allow Coroutine to run in different scope...

        assertAll({ assertThat(gameState).`as`("gameState").isInstanceOf(Lce.Content::class.java) }, {
            assertThat(gameState as Lce.Content).`as`("content").extracting("data").isEqualTo(
                GameOfBlackjack(
                    GameOfBlackjackDto(nickOfPlayer = "junit", gameType = GameType.MANUAL)
                )
            )
        })
    }

    @Test
    fun `should state a successful gameplay as an error when the result from game application contains an error`() = runBlocking {
        var gameState: Lce<GameOfBlackjack>? = null
        val blackjackState = BlackjackState { PlayerName("junit") }
        blackjackState.gameStateConsumer = { newGameState: Lce<GameOfBlackjack> -> gameState = newGameState }
        blackjackState.setBlackjackService(BlackjackService.DefaultBlackjackService(blackjackConsumer = object : BlackjackConsumer {
            override fun play(nick: String, type: GameTypeInternal, gameAction: GameAction?): GameOfBlackjack = GameOfBlackjack(
                GameOfBlackjackDto(
                    nickOfPlayer = "junit",
                    gameType = GameType.MANUAL,
                    error = ErrorDto(message = "noko krasja", provider = "Blodstrupmoen")
                )
            )
        }))

        blackjackState.play(StartManualGame())

        delay(timeMillis = 500) // to allow Coroutine to run in different scope...

        assertAll({ assertThat(gameState).`as`("gameState").isInstanceOf(Lce.Error::class.java) },
            { assertThat(gameState as Lce.Error).`as`("error").extracting(Lce.Error::error).isInstanceOf(GameOfBlackjackException::class.java) },
            {
                assertThat(gameState as Lce.Error).`as`("error.message").extracting(Lce.Error::error).extracting(Throwable::message)
                    .hasToString("Error: 'noko krasja' by 'Blodstrupmoen'!")
            }
        )
    }

    @Test
    fun `should add player name when playing an automatic game of blackjack`(): Unit = runBlocking {
        var gameState: Lce<GameOfBlackjack>? = null
        val blackjackState = BlackjackState { PlayerName("Tor Egil") }

        blackjackState.gameStateConsumer = { newGameState: Lce<GameOfBlackjack> -> gameState = newGameState }
        blackjackState.setBlackjackService(BlackjackService.DefaultBlackjackService(blackjackConsumer = object : BlackjackConsumer {
            override fun play(nick: String, type: GameTypeInternal, gameAction: GameAction?): GameOfBlackjack = GameOfBlackjack(
                GameOfBlackjackDto(nickOfPlayer = "tor-egil", gameType = GameType.AUTOMATIC)
            )
        }))

        do {
            blackjackState.play()
            delay(timeMillis = 100) // to allow Coroutine to run in different scope...
        } while (gameState !is Lce.Content)

        val playerName = (gameState as Lce.Content).data.playerName
        assertThat(playerName).isEqualTo(PlayerName("Tor Egil"))
    }

    @Test
    fun `should add player name when playing a manual game of blackjack`(): Unit = runBlocking {
        var gameState: Lce<GameOfBlackjack>? = null
        val blackjackState = BlackjackState { PlayerName("Tor Egil") }

        blackjackState.gameStateConsumer = { newGameState: Lce<GameOfBlackjack> -> gameState = newGameState }
        blackjackState.setBlackjackService(BlackjackService.DefaultBlackjackService(blackjackConsumer = object : BlackjackConsumer {
            override fun play(nick: String, type: GameTypeInternal, gameAction: GameAction?): GameOfBlackjack = GameOfBlackjack(
                GameOfBlackjackDto(nickOfPlayer = "tor-egil", gameType = GameType.MANUAL)
            )
        }))

        do {
            blackjackState.play(StartManualGame())
            delay(timeMillis = 100) // to allow Coroutine to run in different scope...
        } while (gameState !is Lce.Content)

        val playerName = (gameState as Lce.Content).data.playerName
        assertThat(playerName).isEqualTo(PlayerName("Tor Egil"))
    }
}
