package com.github.jactor.blackjack.compose.service

import com.github.jactor.blackjack.compose.ApplicationConfiguration
import com.github.jactor.blackjack.compose.model.GameStatus
import com.github.jactor.blackjack.compose.model.GameTypeInternal
import com.github.jactor.blackjack.compose.model.PlayerName
import com.github.jactor.blackjack.compose.model.StartManualGame
import com.github.jactor.blackjack.compose.model.Stay
import com.github.jactor.blackjack.compose.model.TakeCard
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assumptions.assumeThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.RepeatedTest
import org.junit.jupiter.api.assertAll
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.PropertySource
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.client.RestClientException
import org.springframework.web.util.UriTemplateHandler

@DisplayName("A com.github.jactor.blackjack.compose.service.BlackjackService")
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

    @RepeatedTest(25)
    fun `should play an automatic game of blackjack`() {
        val gameOfBlackjack = blackjackConsumer.playAutomatic(playerName = PlayerName("jactor"))
        val gameStatus = gameOfBlackjack.status.result
        val dealerScore = gameOfBlackjack.status.dealerScore

        assertAll(
            {
                if (gameStatus != GameStatus.DEALER_WINS && dealerScore <= 21) {
                    assertThat(gameOfBlackjack.status.playerScore).`as`("status.playerScore ($gameOfBlackjack)").isGreaterThanOrEqualTo(17)
                }
            },
            { assertThat(gameOfBlackjack.status.isGameCompleted).`as`("status.isGameCompleted ($gameOfBlackjack)").isTrue() },
            { assertThat(gameOfBlackjack.gameType).`as`("gameType").isEqualTo(GameTypeInternal.AUTOMATIC) }
        )
    }

    @RepeatedTest(25)
    fun `should complete a manual game of blackjack`() {
        var gameOfBlackjack = blackjackConsumer.playManual(playerName = PlayerName("jactor"), gameAction = StartManualGame())

        while (!gameOfBlackjack.status.isGameCompleted) {
            if (gameOfBlackjack.status.dealerScore < 17) {
                gameOfBlackjack = blackjackConsumer.playManual(playerName = PlayerName("jactor"), gameAction = TakeCard(gameOfBlackjack.gameId))
            } else {
                gameOfBlackjack = blackjackConsumer.playManual(playerName = PlayerName("jactor"), gameAction = Stay(gameOfBlackjack.gameId))
            }
        }

        assertAll(
            { assertThat(gameOfBlackjack.playerHand).`as`("playerHand ($gameOfBlackjack)").hasSizeGreaterThanOrEqualTo(2) },
            { assertThat(gameOfBlackjack.dealerHand).`as`("dealerHand ($gameOfBlackjack)").hasSizeGreaterThanOrEqualTo(2) },
            { assertThat(gameOfBlackjack.status.isGameCompleted).`as`("isGameCompleted ($gameOfBlackjack)").isTrue() },
            { assertThat(gameOfBlackjack.gameType).`as`("gameType").isEqualTo(GameTypeInternal.MANUAL) }
        )
    }
}
