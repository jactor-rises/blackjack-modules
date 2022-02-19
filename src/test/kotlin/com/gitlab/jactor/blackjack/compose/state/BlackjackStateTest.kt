package com.gitlab.jactor.blackjack.compose.state

import com.gitlab.jactor.blackjack.compose.consumer.BlackjackConsumer
import com.gitlab.jactor.blackjack.compose.dto.Action
import com.gitlab.jactor.blackjack.compose.dto.ErrorDto
import com.gitlab.jactor.blackjack.compose.dto.GameOfBlackjackDto
import com.gitlab.jactor.blackjack.compose.dto.GameTypeDto
import com.gitlab.jactor.blackjack.compose.model.ActionInternal
import com.gitlab.jactor.blackjack.compose.model.GameOfBlackjack
import com.gitlab.jactor.blackjack.compose.model.GameType
import com.gitlab.jactor.blackjack.compose.model.PlayerName
import com.gitlab.jactor.blackjack.compose.service.BlackjackService
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll

@DisplayName("A com.gitlab.jactor.blackjack.compose.state.BlackjackState")
internal class BlackjackStateTest {

    @Test
    fun `should state a successful gameplay with content of GameOfBlackjack`() = runBlocking {
        var gameState: Lce<GameOfBlackjack>? = null
        val blackjackState = BlackjackState { PlayerName("junit") }
        blackjackState.gameStateConsumer = { newGameState: Lce<GameOfBlackjack> -> gameState = newGameState }
        blackjackState.setBlackjackService(BlackjackService.DefaultBlackjackService(blackjackConsumer = object : BlackjackConsumer {
            override fun play(nick: String, type: GameType, actionInternal: ActionInternal?) = GameOfBlackjack(
                GameOfBlackjackDto(nickOfPlayer = "junit", gameType = GameTypeDto.MANUAL)
            )
        }))


        blackjackState.playManual(Action.START)

        delay(timeMillis = 500) // to allow Coroutine to run in different scope...

        assertAll({ assertThat(gameState).`as`("gameState").isInstanceOf(Lce.Content::class.java) }, {
            assertThat(gameState as Lce.Content).`as`("content").extracting("data").isEqualTo(
                GameOfBlackjack(
                    GameOfBlackjackDto(nickOfPlayer = "junit", gameType = GameTypeDto.MANUAL)
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
            override fun play(nick: String, type: GameType, actionInternal: ActionInternal?) = GameOfBlackjack(
                GameOfBlackjackDto(
                    nickOfPlayer = "junit",
                    gameType = GameTypeDto.MANUAL,
                    error = ErrorDto(message = "noko krasja", provider = "Blodstrupmoen")
                )
            )
        }))

        blackjackState.playManual(Action.START)

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
            override fun play(nick: String, type: GameType, actionInternal: ActionInternal?) = GameOfBlackjack(
                GameOfBlackjackDto(nickOfPlayer = "tor-egil", gameType = GameTypeDto.AUTOMATIC)
            )
        }))

        do {
            blackjackState.playAutomatic()
            delay(timeMillis = 100) // to allow Coroutine to run in different scope...
        } while (!(gameState is Lce.Content))

        val playerName = (gameState as Lce.Content).data.playerName
        assertThat(playerName).isEqualTo(PlayerName("Tor Egil"))
    }

    @Test
    fun `should add player name when playing a manual game of blackjack`(): Unit = runBlocking {
        var gameState: Lce<GameOfBlackjack>? = null
        val blackjackState = BlackjackState { PlayerName("Tor Egil") }

        blackjackState.gameStateConsumer = { newGameState: Lce<GameOfBlackjack> -> gameState = newGameState }
        blackjackState.setBlackjackService(BlackjackService.DefaultBlackjackService(blackjackConsumer = object : BlackjackConsumer {
            override fun play(nick: String, type: GameType, actionInternal: ActionInternal?) = GameOfBlackjack(
                GameOfBlackjackDto(nickOfPlayer = "tor-egil", gameType = GameTypeDto.MANUAL)
            )
        }))

        do {
            blackjackState.playManual(Action.START)
            delay(timeMillis = 100) // to allow Coroutine to run in different scope...
        } while (!(gameState is Lce.Content))

        val playerName = (gameState as Lce.Content).data.playerName
        assertThat(playerName).isEqualTo(PlayerName("Tor Egil"))
    }
}
