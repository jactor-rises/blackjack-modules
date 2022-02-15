package com.gitlab.jactor.blackjack.compose

import com.gitlab.jactor.blackjack.compose.consumer.BlackjackConsumer
import com.gitlab.jactor.blackjack.compose.model.GameOfBlackjack
import com.gitlab.jactor.blackjack.compose.state.BlackjackState
import com.gitlab.jactor.blackjack.compose.state.Lce
import kotlinx.coroutines.Dispatchers
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("A com.gitlab.jactor.blackjack.compose.ApplicationConfiguration")
internal class ApplicationConfigurationTest {

    @Test
    fun `should get the consumer from the application configuration`() {
        val blackjackConsumer = ApplicationConfiguration.fetchBean(BlackjackConsumer::class.java)

        assertThat(blackjackConsumer).isNotNull
    }

    @Test
    fun `should load BlackjackState from application configuration`() {
        var blackjackState: BlackjackState? = null

        ApplicationConfiguration.loadBlackjackState(
            runScope = Dispatchers.Main,
            gameStateConsumer = { newState: Lce<GameOfBlackjack> -> println(newState) },
            blackjackStateConsumer = { newBlackjackState: BlackjackState ->
                blackjackState = newBlackjackState
            }
        )

        do sleep500ms() while (ApplicationConfiguration.isNotActive)

        assertThat(blackjackState).isNotNull
    }

    @Test
    fun `should load properties for game of blackjack`() {
        val gameUrl = ApplicationConfiguration.fetchBean(ApplicationConfiguration.GameUrl::class.java)

        assertThat(gameUrl.url).isEqualTo("http://localhost:1337/blackjack/")
    }

    private fun sleep500ms() {
        Thread.sleep(500)
    }
}
