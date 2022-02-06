package com.gitlab.jactor.blackjack.controller

import com.gitlab.jactor.blackjack.consumer.DeckOfCardsConsumer
import com.gitlab.jactor.blackjack.dto.Action
import com.gitlab.jactor.blackjack.dto.ActionDto
import com.gitlab.jactor.blackjack.dto.CardDto
import com.gitlab.jactor.blackjack.dto.GameOfBlackjackDto
import com.gitlab.jactor.blackjack.dto.GameType
import com.gitlab.jactor.blackjack.model.TestUtil.aDeckOfCardsStartingWith
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.test.annotation.DirtiesContext

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("A com.gitlab.jactor.blackjack.controller.GameController")
internal class GameControllerWithMockedConsumerTest(@Autowired private val testRestTemplate: TestRestTemplate) {

    @MockBean
    private lateinit var deckOfCardsConsumerMock: DeckOfCardsConsumer

    @Test
    @DirtiesContext
    fun `should start a game of blackjack for player`() {
        whenever(deckOfCardsConsumerMock.fetch()).thenReturn(
            aDeckOfCardsStartingWith("SA,DK".split(","))
        )

        val response = testRestTemplate.exchange(
            "/play/jactor",
            HttpMethod.POST,
            HttpEntity(ActionDto(type = GameType.MANUAL, value = Action.START)),
            GameOfBlackjackDto::class.java
        )

        val startedGameOfBlackjackDto = response.body

        assertAll(
            { assertThat(response.statusCode).`as`("status code").isEqualTo(HttpStatus.OK) },
            {
                assertThat(startedGameOfBlackjackDto?.playerHand).`as`("player hand").isEqualTo(
                    listOf(CardDto(suit = "SPADES", value = "A"), CardDto(suit = "DIAMONDS", value = "K"))
                )
            }
        )
    }

    @Test
    fun `should draw a card for a started game on a player`() {
        // Spiller trekker kortene spar to og ruter 3
        // "Magnus" får kortene spar 10 og ruter 4
        // Neste kort (spar konge) vil gå til spilleren...
        whenever(deckOfCardsConsumerMock.fetch()).thenReturn(
            aDeckOfCardsStartingWith("S2,D3,S10,D4,SK".split(","))
        )

        val jactor = "jactor"
        val startResponse = testRestTemplate.exchange(
            "/play/$jactor",
            HttpMethod.POST,
            HttpEntity(ActionDto(type = GameType.MANUAL, value = Action.START)),
            GameOfBlackjackDto::class.java
        )

        val startedGameOfBlackjackDto = startResponse.body

        val runResponse = testRestTemplate.exchange(
            "/play/$jactor",
            HttpMethod.POST,
            HttpEntity(ActionDto(type = GameType.MANUAL, value = Action.HIT)),
            GameOfBlackjackDto::class.java
        )

        val runningGameOfBlackjackDto = runResponse.body

        assertAll(
            { assertThat(startResponse.statusCode).`as`("start status code").isEqualTo(HttpStatus.OK) },
            {
                assertThat(startedGameOfBlackjackDto?.playerHand).`as`("started player hand").isEqualTo(
                    listOf(CardDto(suit = "SPADES", value = "2"), CardDto(suit = "DIAMONDS", value = "3"))
                )
            },
            { assertThat(runResponse.statusCode).`as`("running status code").isEqualTo(HttpStatus.OK) },
            {
                assertThat(runningGameOfBlackjackDto?.playerHand).`as`("running player hand").isEqualTo(
                    listOf(CardDto(suit = "SPADES", value = "2"), CardDto(suit = "DIAMONDS", value = "3"), CardDto(suit = "SPADES", value = "K"))
                )
            }
        )
    }

    @Test
    fun `should complete manual game when action == END`() {
        whenever(deckOfCardsConsumerMock.fetch()).thenReturn(
            aDeckOfCardsStartingWith("S2,D3,S10,D4,S2".split(","))
        )

        val jactor = "jactor"
        val startResponse = testRestTemplate.exchange(
            "/play/$jactor",
            HttpMethod.POST,
            HttpEntity(ActionDto(type = GameType.MANUAL, value = Action.START)),
            GameOfBlackjackDto::class.java
        )

        val startedGameOfBlackjackDto = startResponse.body
        val endResponse = testRestTemplate.exchange(
            "/play/$jactor",
            HttpMethod.POST,
            HttpEntity(ActionDto(type = GameType.MANUAL, value = Action.END)),
            GameOfBlackjackDto::class.java
        )

        val endeddGameOfBlackjackDto = endResponse.body

        assertAll(
            "startResponse",
            { assertThat(startResponse.statusCode).`as`("start status code").isEqualTo(HttpStatus.OK) },
            { assertThat(startedGameOfBlackjackDto?.status?.isGameCompleted).`as`("$startedGameOfBlackjackDto should not be completed").isFalse },
        )

        assertAll(
            "endResponse",
            { assertThat(endResponse.statusCode).`as`("end status code").isEqualTo(HttpStatus.OK) },
            { assertThat(endeddGameOfBlackjackDto?.status?.isGameCompleted).`as`("$startedGameOfBlackjackDto should be completed").isTrue }
        )

        val endAgainResponse = testRestTemplate.exchange(
            "/play/$jactor",
            HttpMethod.POST,
            HttpEntity(ActionDto(type = GameType.MANUAL, value = Action.END)),
            GameOfBlackjackDto::class.java
        )

        assertThat(endAgainResponse.statusCode).`as`("completing already ended game").isEqualTo(HttpStatus.BAD_REQUEST)
    }
}
