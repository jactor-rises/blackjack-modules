package com.gitlab.jactor.blackjack.controller

import com.gitlab.jactor.blackjack.dto.UrlDto
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.test.annotation.DirtiesContext

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("A com.gitlab.jactor.blackjack.controller.UrlController")
internal class UrlControllerTest {

    @Autowired
    private lateinit var testRestTemplate: TestRestTemplate

    @Test
    fun `should fetch default url for the game`() {
        val urlResponse = testRestTemplate.exchange("/url/cards", HttpMethod.GET, null, UrlDto::class.java)

        assertAll(
            { assertThat(urlResponse.statusCode).`as`("status").isEqualTo(HttpStatus.OK) },
            { assertThat(urlResponse.body?.value).`as`("default url").isEqualTo("http://nav-deckofcards.herokuapp.com/shuffle") }
        )
    }

    @Test
    @DirtiesContext
    fun `should set new url for the game (and fetch the new url)`() {
        val someCardUrl = "https://some.card.url"
        val postResponse = testRestTemplate.postForEntity("/url/cards", UrlDto(value = someCardUrl), UrlDto::class.java)
        val getResponse = testRestTemplate.exchange("/url/cards", HttpMethod.GET, null, UrlDto::class.java)

        assertAll(
            { assertThat(postResponse.statusCode).`as`("post status").isEqualTo(HttpStatus.OK) },
            { assertThat(postResponse.body?.value).`as`("post url").isEqualTo(someCardUrl) },
            { assertThat(getResponse.body?.value).`as`("default url").isEqualTo(someCardUrl) }
        )
    }
}
