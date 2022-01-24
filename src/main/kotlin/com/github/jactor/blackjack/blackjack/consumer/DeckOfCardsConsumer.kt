package com.github.jactor.blackjack.blackjack.consumer

import com.github.jactor.blackjack.blackjack.model.DeckOfCards
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate

@Component
class DeckOfCardsConsumer(private val restTemplate: RestTemplate) {
    fun fetchCardsForGame(): DeckOfCards {
        TODO("Not yet implemented")
    }
}
