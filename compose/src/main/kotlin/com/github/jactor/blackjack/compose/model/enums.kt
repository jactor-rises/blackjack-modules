package com.github.jactor.blackjack.compose.model

import com.github.jactor.blackjack.compose.dto.Action
import com.github.jactor.blackjack.compose.dto.GameTypeDto

enum class Face(val value: String) {
    TWO("2"), THREE("3"), FOUR("4"), FIVE("5"), SIX("6"), SEVEN("7"), EIGHT("8"), NINE("9"), TEN("10"),
    JACK("J"), QUEEN("Q"), KING("K"), ACE("A");
}

enum class GameStatus { PLAYER_WINS, DEALER_WINS }

enum class Suit(val char: Char) { HEARTS('H'), DIAMONDS('D'), SPADES('S'), CLUBS('C'); }

enum class GameType {
    AUTOMATIC, MANUAL;

    fun asDto(): GameTypeDto {
        return GameTypeDto.values().first { it.name == this.name }
    }

    private fun isSameAs(gameTypeDto: GameTypeDto): Boolean {
        return this.name == gameTypeDto.name
    }

    companion object {
        fun valueOf(gameTypeDto: GameTypeDto): GameType {
            values().forEach {
                if (it.isSameAs(gameTypeDto)) {
                    return it
                }
            }

            throw IllegalStateException("Unknown game type: $gameTypeDto")
        }
    }
}

enum class ActionInternal {
    START, HIT, END;

    fun toDto() = Action.values().first { it.name == this.name }
}

enum class GameOption { PLAYER_NAME, QUIT, CONTINUE }
