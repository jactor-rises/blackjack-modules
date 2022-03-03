package com.github.jactor.blackjack.compose.service

import com.github.jactor.blackjack.compose.consumer.BlackjackConsumer
import com.github.jactor.blackjack.compose.dto.GameOfBlackjackDto
import com.github.jactor.blackjack.compose.model.ActionInternal
import com.github.jactor.blackjack.compose.model.GameOfBlackjack
import com.github.jactor.blackjack.compose.model.GameType
import com.github.jactor.blackjack.compose.model.PlayerName
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("A com.github.jactor.blackjack.compose.service.BlackjackService")
internal class BlackjackServiceTest {

    private val blackjackService = BlackjackService.DefaultBlackjackService(
        object : BlackjackConsumer {
            override fun play(nick: String, type: GameType, actionInternal: ActionInternal?) = GameOfBlackjack(
                GameOfBlackjackDto(nickOfPlayer = nick, gameType = type.asDto())
            )
        }
    )

    @Test
    fun `should add player name when playing an automatic game of blackjack`() {
        val gameOfBlackjack = blackjackService.playAutomatic(PlayerName("Tor Egil"))

        assertThat(gameOfBlackjack.playerName).isEqualTo(PlayerName("Tor Egil"))
    }

    @Test
    fun `should add player name when playing a manual game of blackjack`() {
        val gameOfBlackjack = blackjackService.playManual(PlayerName("Tor Egil"), ActionInternal.START)

        assertThat(gameOfBlackjack.playerName).isEqualTo(PlayerName("Tor Egil"))
    }
}