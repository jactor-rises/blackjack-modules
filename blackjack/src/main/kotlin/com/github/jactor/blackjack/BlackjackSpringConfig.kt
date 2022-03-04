package com.github.jactor.blackjack

import com.github.jactor.blackjack.model.CardsUrl
import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Info
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource
import org.springframework.web.client.RestTemplate

@Configuration
@OpenAPIDefinition(info = Info(title = "blackjack", description = "Spill blackjack mot \"Banken\"", version = "v0"))
@PropertySource(value = ["classpath:blackjack.properties"])
class BlackjackSpringConfig {

    @Value("\${blackjack.cards.url}")
    val cardsUrl: String = ""

    @Bean
    fun restTemplate(): RestTemplate {
        return RestTemplate()
    }

    @Bean
    fun CardsUrl(): CardsUrl {
        return CardsUrl(cardsUrl)
    }
}
