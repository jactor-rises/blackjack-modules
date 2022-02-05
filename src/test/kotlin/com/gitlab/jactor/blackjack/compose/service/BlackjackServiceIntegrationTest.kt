package com.gitlab.jactor.blackjack.compose.service

import com.gitlab.jactor.blackjack.compose.ApplicationConfiguration
import com.gitlab.jactor.blackjack.compose.model.GameType
import com.gitlab.jactor.blackjack.compose.model.PlayerName
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assumptions.assumeThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.PropertySource
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.client.RestClientException
import org.springframework.web.util.UriTemplateHandler

@DisplayName("A com.gitlab.jactor.blackjack.compose.service.BlackjackService")
@PropertySource("classpath:blackjack.properties")
internal class BlackjackServiceIntegrationTest {

    private val testRestTemplate = TestRestTemplate(
        RestTemplateBuilder().uriTemplateHandler(ApplicationConfiguration.fetchBean(UriTemplateHandler::class.java))
    )

    private val blackjackConsumer = ApplicationConfiguration.fetchBean(BlackjackService::class.java)

    @BeforeEach
    fun `assume blackjack is running`() {
        val response: ResponseEntity<String> = try {
            testRestTemplate.getForEntity("actuator/health", String::class.java)
        } catch (e: RestClientException) {
            ResponseEntity.internalServerError().body(e.message)
        }

        assumeThat(response.statusCode).`as`("response.status").isEqualTo(HttpStatus.OK)
        assumeThat(response.body).`as`("response.body").contains(""""status":"UP"""")
    }

    @Test
    fun `should play an automatic game of blackjack`() {
        val gameOfBlackjack = blackjackConsumer.play(type = GameType.AUTOMATIC, playerName = PlayerName("jactor"))

        assertAll(
            { assertThat(gameOfBlackjack.status.playerScore).`as`("status.playerScore ($gameOfBlackjack)").isGreaterThanOrEqualTo(17) },
            { assertThat(gameOfBlackjack.status.isGameCompleted).`as`("status.isGameCompleted ($gameOfBlackjack)").isTrue() }
        )
    }

    @Test
    fun `should play a manual game of blackjack`() {
        val gameOfBlackjack = blackjackConsumer.play(type = GameType.MANUAL, playerName = PlayerName("jactor"))

        assertAll(
            { assertThat(gameOfBlackjack.playerHand).`as`("playerHand ($gameOfBlackjack)").hasSize(2) },
            { assertThat(gameOfBlackjack.status.playerScore).`as`("status.playerScore ($gameOfBlackjack)").isLessThanOrEqualTo(21) }
        )
    }
}
