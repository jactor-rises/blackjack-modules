package com.github.jactor.blackjack.controller

import com.github.jactor.blackjack.dto.Action
import com.github.jactor.blackjack.dto.ActionDto
import com.github.jactor.blackjack.dto.GameOfBlackjackDto
import com.github.jactor.blackjack.dto.GameStatus
import com.github.jactor.blackjack.dto.GameType
import com.github.jactor.blackjack.dto.WelcomeDto
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.RepeatedTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("A com.github.jactor.blackjack.controller.GameController")
internal class GameControllerTest(@Autowired private val testRestTemplate: TestRestTemplate) {

    @Test
    fun `should get start messages`() {
        val response = testRestTemplate.exchange("/", HttpMethod.GET, null, WelcomeDto::class.java)

        assertAll(
            { assertThat(response.statusCode).`as`("status code").isEqualTo(HttpStatus.OK) },
            { assertThat(response.body?.message).`as`("welcome message").isEqualTo("Velkommen til en runde med Blackjack") },
            {
                assertThat(response.body?.howTo).`as`("how to").isEqualTo(
                    """
                    * Gjør en post til endepunkt '/play/{kallenavn}' og med GameType.AUTOMATIC for å utføre et helautmatisk spill (Ace = 11 poeng)
                    * Gjør en post til endepunkt '/play/{kallenavn}' og med GameType.MANUAL samt Action.START for å starte et spill (Ace er 11 eller 1 poeng)
                      * Videre spill på samme kallenavn ved post til endepunkt '/play/{kallenavn}' og med GameType.MANUAL samt Action.START, HIT eller END
                    """.trimIndent()
                )
            }
        )
    }

    @Test
    fun `should perform a game of blackjack for player with nick`() {
        val kallenavn = "jactor"
        val response = testRestTemplate.exchange(
            "/play/$kallenavn",
            HttpMethod.POST,
            HttpEntity(ActionDto(type = GameType.AUTOMATIC)),
            GameOfBlackjackDto::class.java
        )

        val gameOfBlackjackDto = response.body

        assertAll(
            { assertThat(response.statusCode).`as`("status code").isEqualTo(HttpStatus.OK) },
            { assertThat(gameOfBlackjackDto?.nickOfPlayer).`as`("kallenavn").isEqualTo(kallenavn) },
            { assertThat(gameOfBlackjackDto?.dealerHand).`as`("Magnus sine kort").hasSizeGreaterThanOrEqualTo(2) },
            { assertThat(gameOfBlackjackDto?.playerHand).`as`("$kallenavn sine kort").hasSizeGreaterThanOrEqualTo(2) },
            { assertThat(gameOfBlackjackDto?.status?.result).`as`("vinner").isNotNull() }
        )
    }

    @RepeatedTest(25)
    fun `should play an automatic game of blackjack`() {
        val response = testRestTemplate.exchange(
            "/play/jactor",
            HttpMethod.POST,
            HttpEntity(ActionDto(type = GameType.AUTOMATIC)),
            GameOfBlackjackDto::class.java
        )

        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)

        val gameOfBlackjack = response.body!!
        val gameStatus = gameOfBlackjack.status?.result!!
        val dealerScore = gameOfBlackjack.status?.dealerScore!!

        assertAll(
            {
                if (gameStatus != GameStatus.DEALER_WINS && dealerScore <= 21) {
                    assertThat(gameOfBlackjack.status?.playerScore).`as`("status.playerScore ($gameOfBlackjack)").isGreaterThanOrEqualTo(17)
                }
            },
            { assertThat(gameOfBlackjack.status?.isGameCompleted).`as`("status.isGameCompleted ($gameOfBlackjack)").isTrue() },
            { assertThat(response.body?.gameType).`as`("gameType").isEqualTo(GameType.AUTOMATIC) }
        )
    }

    @RepeatedTest(25)
    fun `should play an manual game of blackjack`() {
        val response = testRestTemplate.exchange(
            "/play/jactor",
            HttpMethod.POST,
            HttpEntity(ActionDto(type = GameType.MANUAL, value = Action.START)),
            GameOfBlackjackDto::class.java
        )

        assertAll(
            { assertThat(response.body?.playerHand).`as`("playerHand (${response.body})").hasSize(2) },
            { assertThat(response.body?.status?.dealerScore).`as`("status.dealerScore (${response.body})").isLessThanOrEqualTo(21) },
            { assertThat(response.body?.status?.playerScore).`as`("status.playerScore (${response.body})").isLessThanOrEqualTo(21) },
            { assertThat(response.body?.gameType).`as`("gameType").isEqualTo(GameType.MANUAL) }
        )
    }
}
