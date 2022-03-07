package com.github.jactor.blackjack.dto

import com.fasterxml.jackson.databind.ObjectMapper
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll

internal class DtoMappingTest {
    private val objectMapper = ObjectMapper()

    @Test
    fun `should map gameId of GameOfBlackjackDto`() {
        val json = """
            {
              "gameId":"123xyz"
            }
        """.trimIndent()

        val dto = objectMapper.readValue(json, GameOfBlackjackDto::class.java)

        assertAll(
            { assertThat(dto.gameId).`as`("gameId").isEqualTo("123xyz") }
        )
    }

    @Test
    fun `should map ErrorDto`() {
        val json = """
            {
              "message":"say what?",
              "provider":"your mama"
            }
        """.trimIndent()

        val dto = objectMapper.readValue(json, ErrorDto::class.java)

        assertAll(
            { assertThat(dto.message).`as`("message").isEqualTo("say what?") },
            { assertThat(dto.provider).`as`("provider").isEqualTo("your mama") }
        )
    }
}
