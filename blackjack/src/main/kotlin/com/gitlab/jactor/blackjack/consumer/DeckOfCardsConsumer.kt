package com.gitlab.jactor.blackjack.consumer

import com.gitlab.jactor.blackjack.dto.CardDto
import com.gitlab.jactor.blackjack.model.Card
import com.gitlab.jactor.blackjack.model.CardsUrl
import com.gitlab.jactor.blackjack.model.DeckOfCards
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate

@Component
class DeckOfCardsConsumer(private val restTemplate: RestTemplate) {
    fun fetch(): DeckOfCards {
        val response = restTemplate.exchange(CardsUrl.url, HttpMethod.GET, null, typereferansenErListeMedKort())
        return DeckOfCards(deck = ArrayList(response.body?.map { Card(it) } ?: emptyList()))
    }

    companion object {
        private fun typereferansenErListeMedKort(): ParameterizedTypeReference<List<CardDto>> {
            return object : ParameterizedTypeReference<List<CardDto>>() {}
        }
    }
}
