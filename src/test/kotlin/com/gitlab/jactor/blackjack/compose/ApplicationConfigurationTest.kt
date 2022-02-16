package com.gitlab.jactor.blackjack.compose

import com.gitlab.jactor.blackjack.compose.consumer.BlackjackConsumer
import com.gitlab.jactor.blackjack.compose.model.PlayerName
import com.gitlab.jactor.blackjack.compose.service.BlackjackService
import com.gitlab.jactor.blackjack.compose.state.BlackjackState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
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
    fun `should set BlackjackService from application configuration`() {
        val blackjackState = BlackjackState(playerName = PlayerName("jactor"), runScope = Dispatchers.Main)

        ApplicationConfiguration.setBlackjackService(
            blackjackState = blackjackState,
            defaultScope = Dispatchers.Main
        )

        do delay500ms() while (!ApplicationConfiguration.annotationConfigApplicationContext.isActive)

        assertThat(blackjackState.blackjackService).isInstanceOf(BlackjackService.DefaultBlackjackService::class.java)
    }

    @Test
    fun `should load properties for game of blackjack`() {
        val gameUrl = ApplicationConfiguration.fetchBean(ApplicationConfiguration.GameUrl::class.java)

        assertThat(gameUrl.url).isEqualTo("http://localhost:1337/blackjack/")
    }

    private fun delay500ms() = runBlocking{
        delay(timeMillis = 500)
    }
}
