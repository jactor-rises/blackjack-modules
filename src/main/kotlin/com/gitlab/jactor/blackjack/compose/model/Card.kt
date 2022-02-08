package com.gitlab.jactor.blackjack.compose.model

import com.gitlab.jactor.blackjack.compose.dto.CardDto

data class Card(val face: Face, val suit: Suit) {
    val text: String get() = "${suit}(${face})"
    val imageFileName: String get() = "${suit.char}${face.value}.png".lowercase()
    val color: Color get() = when(suit) {
        Suit.SPADES, Suit.CLUBS -> Color.BLACK
        Suit.DIAMONDS, Suit.HEARTS -> Color.RED
    }

    constructor(cardDto: CardDto) : this(
        face = Face.values().first { it.value == cardDto.value },
        suit = Suit.valueOf(cardDto.suit)
    )

   enum class Color { BLACK, RED }
}
