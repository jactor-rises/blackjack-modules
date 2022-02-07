package com.gitlab.jactor.blackjack.compose.model

enum class Face(val value: String) {
    TWO("2"), THREE("3"), FOUR("4"), FIVE("5"), SIX("6"), SEVEN("7"), EIGHT("8"), NINE("9"), TEN("10"),
    JACK("J"), QUEEN("Q"), KING("K"), ACE("A");
}

enum class GameStatus { PLAYER_WINS, DEALER_WINS }

enum class Suit { HEARTS, DIAMONDS, SPADES, CLUBS }

enum class GameType {
    AUTOMATIC, MANUAL;

    fun asDto(): com.gitlab.jactor.blackjack.compose.dto.GameType {
        return com.gitlab.jactor.blackjack.compose.dto.GameType.values().first { it.name == this.name }
    }
}

enum class Action {
    START, HIT, END;

    fun toDto() = com.gitlab.jactor.blackjack.compose.dto.Action.values().first { it.name == this.name }
}
