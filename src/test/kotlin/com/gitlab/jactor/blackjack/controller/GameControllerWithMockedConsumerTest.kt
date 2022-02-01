package com.gitlab.jactor.blackjack.controller

import com.gitlab.jactor.blackjack.consumer.DeckOfCardsConsumer
import com.gitlab.jactor.blackjack.dto.CardDto
import com.gitlab.jactor.blackjack.dto.StartedGameOfBlackjackDto
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
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("A com.gitlab.jactor.blackjack.controller.GameController")
internal class GameControllerWithMockedConsumerTest {

    @MockBean
    private lateinit var deckOfCardsConsumer: DeckOfCardsConsumer

    @Autowired
    private lateinit var testRestTemplate: TestRestTemplate

    @Test
    fun `should start a game of blackjack for player`() {
        whenever(deckOfCardsConsumer.fetch()).thenReturn(
            aDeckOfCardsStartingWith("SA,DK".split(","))
        )

        val response = testRestTemplate.exchange("/start/jactor", HttpMethod.POST, null, StartedGameOfBlackjackDto::class.java)
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
}
