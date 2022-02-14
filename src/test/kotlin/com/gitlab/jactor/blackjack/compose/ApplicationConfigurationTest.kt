package com.gitlab.jactor.blackjack.compose

import com.gitlab.jactor.blackjack.compose.consumer.BlackjackConsumer
import com.gitlab.jactor.blackjack.compose.state.BlackjackState
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

        ApplicationConfiguration.loadBlackjackState(Dispatchers.Main) { newBlackjackState: BlackjackState ->
            blackjackState = newBlackjackState
        }

        do sleep500ms() while (ApplicationConfiguration.isNotActive)

        assertThat(blackjackState).isNotNull
    }
}

private fun sleep500ms() {
    Thread.sleep(500)
}

