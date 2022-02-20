package com.gitlab.jactor.blackjack.controller

import com.gitlab.jactor.blackjack.dto.UrlDto
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
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

    @Value("\${blackjack.cards.url}")
    private lateinit var defaultUrl: String

    @Test
    fun `should fetch default url for the game`() {
        val urlResponse = testRestTemplate.exchange("/url/cards", HttpMethod.GET, null, UrlDto::class.java)

        assertAll(
            { assertThat(urlResponse.statusCode).`as`("status").isEqualTo(HttpStatus.OK) },
            { assertThat(urlResponse.body?.value).`as`("default url").isEqualTo(defaultUrl) }
        )
    }

    @Test
    @DirtiesContext
    fun `should set new url for the game (and fetch the new url)`() {
        val someCardUrl = "https://some.card.url"
        val getDefaultResponse = testRestTemplate.exchange("/url/cards", HttpMethod.GET, null, UrlDto::class.java)
        val postResponse = testRestTemplate.postForEntity("/url/cards", UrlDto(value = someCardUrl), UrlDto::class.java)
        val getPostResponse = testRestTemplate.exchange("/url/cards", HttpMethod.GET, null, UrlDto::class.java)

        assertAll(
            { assertThat(getDefaultResponse.body?.value).`as`("default url").isEqualTo(defaultUrl) },
            { assertThat(postResponse.statusCode).`as`("post status").isEqualTo(HttpStatus.OK) },
            { assertThat(postResponse.body?.value).`as`("post url").isEqualTo(someCardUrl) },
            { assertThat(getPostResponse.body?.value).`as`("url as posted").isEqualTo(someCardUrl) }
        )
    }
}
