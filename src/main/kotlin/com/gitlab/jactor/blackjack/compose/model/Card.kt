package com.gitlab.jactor.blackjack.compose.model

import com.gitlab.jactor.blackjack.compose.dto.CardDto

data class Card(val face: Face, val suit: Suit) {
    constructor(cardDto: CardDto) : this(
        face = Face.values().first { it.value == cardDto.value },
        suit = Suit.valueOf(cardDto.suit)
    )
}
