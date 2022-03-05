package com.github.jactor.blackjack

import com.github.jactor.blackjack.consumer.DeckOfCardsConsumer
import com.github.jactor.blackjack.model.CardsRestTemplate
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
    fun restTemplate(): RestTemplate = RestTemplate()

    @Bean
    fun cardsRestTemplate(restTemplate: RestTemplate): CardsRestTemplate = CardsRestTemplate(restTemplate, cardsUrl)

    @Bean
    fun deckOfCardsConsumer(cardsRestTemplate: CardsRestTemplate): DeckOfCardsConsumer {
        if (cardsRestTemplate.urlStartsWithHttp) {
            return DeckOfCardsConsumer.DefaultDeckOfCardsConsumer(cardsRestTemplate)
        }

        return DeckOfCardsConsumer.ConstructShuffledCards()
    }
}
