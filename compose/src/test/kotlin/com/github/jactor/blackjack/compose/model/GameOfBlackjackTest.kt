package com.github.jactor.blackjack.compose.model

import com.github.jactor.blackjack.dto.GameOfBlackjackDto
import com.github.jactor.blackjack.dto.GameStatus
import com.github.jactor.blackjack.dto.GameType
import com.github.jactor.blackjack.dto.StatusDto
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatIllegalStateException
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll

@DisplayName("A com.github.jactor.blackjack.compose.model.GameOfBlackjack")
internal class GameOfBlackjackTest {

    @Test
    fun `should add player name to game of blackjack`() {
        val gameOfBlackjack = GameOfBlackjack(GameOfBlackjackDto(nickOfPlayer = "jactor", gameType = GameType.AUTOMATIC))
            .add(PlayerName("jactor"))

        assertThat(gameOfBlackjack.playerName).isEqualTo(PlayerName("jactor"))
    }

    @Test
    fun `should fail when getting playerName when name is not added`() {
        assertThatIllegalStateException().isThrownBy { GameOfBlackjack(GameOfBlackjackDto(gameType = GameType.AUTOMATIC)).playerName }
            .withMessage("Player name has not been added!")
    }

    @Test
    fun `should not fail when trying to add a playerName which does not match against the nickOfPlayer`() {
        val gameOfBlackjack = GameOfBlackjack(
            GameOfBlackjackDto(
                nickOfPlayer = "turbo",
                gameType = GameType.AUTOMATIC
            )
        ).add(PlayerName(name = "jactor"))

        assertAll(
            { assertThat(gameOfBlackjack.playerName).`as`("player name").isEqualTo(PlayerName(name = "jactor")) },
            { assertThat(gameOfBlackjack.nickOfPlayer).`as`("nick of player").isEqualTo("turbo") }
        )
    }

    @Test
    fun `should display winner`() {
        val gameOfBlackjack = GameOfBlackjack(
            GameOfBlackjackDto(
                nickOfPlayer = "tor-egil",
                status = StatusDto(
                    result = GameStatus.PLAYER_WINS,
                    playerScore = 20,
                    dealerScore = 22,
                    isGameCompleted = true
                )
            )
        ).add(PlayerName("tor egil"))

        assertThat(gameOfBlackjack.displayWinner()).isEqualTo("Tor Egil er vinneren!")
    }

    @Test
    fun `should display blackjack`() {
        val gameOfBlackjack = GameOfBlackjack(
            GameOfBlackjackDto(
                nickOfPlayer = "tor-egil",
                status = StatusDto(
                    result = GameStatus.PLAYER_WINS,
                    playerScore = 21,
                    dealerScore = 22,
                    isGameCompleted = true
                )
            )
        ).add(PlayerName("tor egil"))

        assertThat(gameOfBlackjack.displayWinner()).isEqualTo("Tor Egil fikk blackjack!")
    }
}
