package com.gitlab.jactor.blackjack.compose

import com.gitlab.jactor.blackjack.compose.consumer.BlackjackConsumer
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
}
