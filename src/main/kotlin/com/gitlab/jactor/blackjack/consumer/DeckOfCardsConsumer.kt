package com.gitlab.jactor.blackjack.consumer

import com.gitlab.jactor.blackjack.model.DeckOfCards
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate

@Component
class DeckOfCardsConsumer(private val restTemplate: RestTemplate) {
    fun fetch(): DeckOfCards {
        TODO("Not yet implemented")
    }
}
