package com.gitlab.jactor.blackjack.model

enum class Suit(private val character: Char) {
    HEARTS('H'), DIAMONDS('D'), SPADES('S'), CLUBS('C');

    companion object {
        fun from(char: Char): Suit = values().toList().stream()
            .filter { char == it.character }
            .findFirst().orElseThrow { IllegalArgumentException("No suit with character '$char'!") }
    }
}