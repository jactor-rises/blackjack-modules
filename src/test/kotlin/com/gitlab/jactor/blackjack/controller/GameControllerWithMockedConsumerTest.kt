package com.gitlab.jactor.blackjack.controller

import com.gitlab.jactor.blackjack.consumer.DeckOfCardsConsumer
import com.gitlab.jactor.blackjack.dto.ActionDto
import com.gitlab.jactor.blackjack.dto.CardDto
import com.gitlab.jactor.blackjack.dto.GameOfBlackjackDto
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
internal class GameControllerWithMockedConsumerTest {

    @MockBean
    private lateinit var deckOfCardsConsumer: DeckOfCardsConsumer

    @Autowired
    private lateinit var testRestTemplate: TestRestTemplate

    @Test
    @DirtiesContext
    fun `should start a game of blackjack for player`() {
        whenever(deckOfCardsConsumer.fetch()).thenReturn(
            aDeckOfCardsStartingWith("SA,DK".split(","))
        )

        val response = testRestTemplate.exchange("/start/jactor", HttpMethod.POST, null, GameOfBlackjackDto::class.java)
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
    @DirtiesContext
    fun `skal trekke et nytt kort for et spill med kallenavn`() {
        // Spiller trekker kortene spar to og ruter 3
        // "Magnus" får kortene spar 10 og ruter 4
        // Neste kort (spar ess) vil gå til spilleren...
        whenever(deckOfCardsConsumer.fetch()).thenReturn(
            aDeckOfCardsStartingWith("S2,D3,S10,D4,SA".split(","))
        )

        val jactor = "jactor"
        val startResponse = testRestTemplate.exchange("/start/$jactor", HttpMethod.POST, null, GameOfBlackjackDto::class.java)
        val startedGameOfBlackjackDto = startResponse.body

        val runResponse = testRestTemplate.exchange(
            "/running/$jactor",
            HttpMethod.POST,
            HttpEntity(ActionDto(isDrawNewCard = true)),
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
                    listOf(CardDto(suit = "SPADES", value = "2"), CardDto(suit = "DIAMONDS", value = "3"), CardDto(suit = "SPADES", value = "A"))
                )
            }
        )
    }
}
