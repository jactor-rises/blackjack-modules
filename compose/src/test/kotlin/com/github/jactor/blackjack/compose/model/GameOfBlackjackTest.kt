package com.github.jactor.blackjack.compose.model

import com.github.jactor.blackjack.compose.dto.GameOfBlackjackDto
import com.github.jactor.blackjack.compose.dto.GameTypeDto
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatIllegalStateException
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("A com.github.jactor.blackjack.compose.model.GameOfBlackjack")
internal class GameOfBlackjackTest {

    @Test
    fun `should add player name to game of blackjack`() {
        val gameOfBlackjack = GameOfBlackjack(GameOfBlackjackDto(nickOfPlayer = "jactor", gameType = GameTypeDto.AUTOMATIC))
            .with(PlayerName("jactor"))

        assertThat(gameOfBlackjack.playerName).isEqualTo(PlayerName("jactor"))
    }

    @Test
    fun `should fail when getting playerName when name is not added`() {
        assertThatIllegalStateException().isThrownBy { GameOfBlackjack(GameOfBlackjackDto(gameType = GameTypeDto.AUTOMATIC)).playerName }
            .withMessage("Player name has not been added!")
    }

    @Test
    fun `should fail when trying to add a playerName which does not match against the nichOfPlayer`() {
        assertThatIllegalStateException().isThrownBy {
            GameOfBlackjack(
                GameOfBlackjackDto(
                    nickOfPlayer = "turbo",
                    gameType = GameTypeDto.AUTOMATIC
                )
            ).with(PlayerName("jactor"))
        }
            .withMessage("The nick of added player do not match the game nick (turbo)!")
    }
}
