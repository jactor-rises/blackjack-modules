package com.github.jactor.blackjack.model

enum class Face(internal val value: String) {
    TWO("2"), THREE("3"), FOUR("4"), FIVE("5"), SIX("6"), SEVEN("7"), EIGHT("8"), NINE("9"), TEN("10"),
    JACK("J"), QUEEN("Q"), KING("K"), ACE("A");

    val isAce: Boolean get() = ACE == this

    companion object {
        fun from(value: String): Face = values().toList().stream()
            .filter { it.value == value }
            .findFirst().orElseThrow { IllegalArgumentException("No card face with value '$value'!") }
    }
}
