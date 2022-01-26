package com.gitlab.jactor.blackjack.model

import com.gitlab.jactor.blackjack.dto.CardDto

data class Card(val suit: Suit, val face: Face) {
    constructor(card: CardDto): this(suit = Suit.valueOf(card.suit), face = Face.from(card.value))
    fun toDto() = CardDto(suit = suit.name, value = face.value)
}
