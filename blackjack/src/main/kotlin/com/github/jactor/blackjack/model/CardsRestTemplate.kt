package com.github.jactor.blackjack.model

import com.github.jactor.blackjack.dto.CardDto
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpMethod
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.exchange

class CardsRestTemplate(private var restTemplate: RestTemplate = RestTemplate(), var url: String = "") {

    fun getCards(): List<CardDto> = restTemplate.exchange<List<CardDto>>(
        url = url,
        method = HttpMethod.GET,
        requestEntity = null,
        typereferansenErListeMedKort()
    ).body ?: emptyList()

    companion object {
        private fun typereferansenErListeMedKort(): ParameterizedTypeReference<List<CardDto>> {
            return object : ParameterizedTypeReference<List<CardDto>>() {}
        }
    }
}
