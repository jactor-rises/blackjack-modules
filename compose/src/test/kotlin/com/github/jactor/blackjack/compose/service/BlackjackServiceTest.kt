package com.github.jactor.blackjack.compose.service

import com.github.jactor.blackjack.compose.consumer.BlackjackConsumer
import com.github.jactor.blackjack.compose.model.GameAction
import com.github.jactor.blackjack.compose.model.GameOfBlackjack
import com.github.jactor.blackjack.compose.model.GameTypeInternal
import com.github.jactor.blackjack.compose.model.PlayerName
import com.github.jactor.blackjack.compose.model.StartManualGame
import com.github.jactor.blackjack.dto.GameOfBlackjackDto
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("A com.github.jactor.blackjack.compose.service.BlackjackService")
internal class BlackjackServiceTest {

    private val blackjackService = BlackjackService.DefaultBlackjackService(
        object : BlackjackConsumer {
            override fun play(nick: String, type: GameTypeInternal, gameAction: GameAction?): GameOfBlackjack = GameOfBlackjack(
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
        val gameOfBlackjack = blackjackService.playManual(PlayerName("Tor Egil"), StartManualGame())

        assertThat(gameOfBlackjack.playerName).isEqualTo(PlayerName("Tor Egil"))
    }
}
