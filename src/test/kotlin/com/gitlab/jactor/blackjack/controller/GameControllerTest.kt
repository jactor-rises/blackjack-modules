package com.gitlab.jactor.blackjack.controller

import com.gitlab.jactor.blackjack.dto.GameOfBlackjackDto
import com.gitlab.jactor.blackjack.dto.WelcomeDto
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("A com.gitlab.jactor.blackjack.controller.GameController")
internal class GameControllerTest {

    @Autowired
    private lateinit var testRestTemplate: TestRestTemplate

    @Test
    fun `should get start messages`() {
        val response = testRestTemplate.exchange("/", HttpMethod.GET, null, WelcomeDto::class.java)

        assertAll(
            { assertThat(response.statusCode).`as`("status code").isEqualTo(HttpStatus.OK) },
            { assertThat(response.body?.message).`as`("welcome message").isEqualTo("Velkommen til en runde med Blackjack") },
            {
                assertThat(response.body?.howTo).`as`("how to").isEqualTo(
                    """
                    * Gjør en post til endepunkt '/play/{kallenavn} for å utføre et helautmatisk spill (Ace = 11 poeng)
                    * Gjør en post til endepunkt '/start/{kallenavn} for å starte et spill (Ace er 11 eller 1 poeng)
                      * Videre spill på samme kallenavn er post til endepunkt '/running/{kallenavn}'
                    """.trimIndent()
                )
            }
        )
    }

    @Test
    fun `should perform a game of blackjack for player with nick`() {
        val kallenavn = "jactor"
        val response = testRestTemplate.exchange("/play/$kallenavn", HttpMethod.POST, null, GameOfBlackjackDto::class.java)
        val gameOfBlackjackDto = response.body

        assertAll(
            { assertThat(response.statusCode).`as`("status code").isEqualTo(HttpStatus.OK) },
            { assertThat(gameOfBlackjackDto?.nickOfPlayer).`as`("kallenavn").isEqualTo(kallenavn) },
            { assertThat(gameOfBlackjackDto?.dealerHand).`as`("Magnus sine kort").hasSizeGreaterThanOrEqualTo(2) },
            { assertThat(gameOfBlackjackDto?.playerHand).`as`("$kallenavn sine kort").hasSizeGreaterThanOrEqualTo(2) },
            { assertThat(gameOfBlackjackDto?.status?.playerScore).`as`("poeng til $kallenavn").isGreaterThanOrEqualTo(17) },
            { assertThat(gameOfBlackjackDto?.status?.status).`as`("vinner").isNotNull() }
        )
    }
}
