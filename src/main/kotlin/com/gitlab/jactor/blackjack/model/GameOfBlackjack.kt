package com.gitlab.jactor.blackjack.model

import com.gitlab.jactor.blackjack.dto.GameOfBlackjackDto
import com.gitlab.jactor.blackjack.dto.ResultDto

data class GameOfBlackjack(val deckOfCards: DeckOfCards, val nick: String) {
    internal val playerHand: MutableList<Card> = deckOfCards.take(noOfCards = 2)
    internal val dealerHand: MutableList<Card> = deckOfCards.take(noOfCards = 2)

    private val dealerScore: Int get() = dealerHand.sumOf { fetchValue(it.face) }
    private val playerScore: Int get() = playerHand.sumOf { fetchValue(it.face) }
    private val winner: String
        get() = when (fetchResult()) {
            Result.PLAYER -> nick
            Result.DEALER -> "Magnus"
        }

    fun completeGame(): GameOfBlackjack {
        if (dealerScore > 21) { // will happen if the dealer get 2 aces
            return this
        }

        while (playerScore < 17) {
            playerHand.add(deckOfCards.takeCard())
        }

        while (playerScore in dealerScore..20) {
            dealerHand.add(deckOfCards.takeCard())
        }

        return this
    }

    fun logResult(): GameOfBlackjack {
        println(
            """
                Vinner: $nick
                
                Magnus | $dealerScore | ${dealerHand.toString().removePrefix("[").removeSuffix("]").replace(", ", ",")}
                $nick | $playerScore | ${playerHand.toString().removePrefix("[").removeSuffix("]").replace(", ", ",")}
            """.trimIndent()
        )

        return this
    }

    fun toDto() = GameOfBlackjackDto(
        nickOfPlayer = nick,
        dealerHand = dealerHand.map { it.toDto() },
        playerHand = playerHand.map { it.toDto() },
        resultat = ResultDto(
            winner = winner,
            dealerScore = dealerScore,
            playerScore = playerScore
        )
    )

    private fun fetchValue(face: Face): Int {
        if (Face.ACE == face) {
            return 11
        } else if (face.value.matches("\\d+".toRegex())) {
            return face.value.toInt()
        }

        return 10 // King, Queen, Jack
    }

    private fun fetchResult(): Result {
        val dealerFinalScore = dealerScore
        val playerFinalScore = playerScore

        if (playerFinalScore == 21) {
            return Result.PLAYER
        }

        if (dealerFinalScore == 21) {
            return Result.DEALER
        }

        if (playerFinalScore > 21) {
            return Result.DEALER
        }

        if (dealerFinalScore in (playerFinalScore + 1)..20) {
            return Result.DEALER
        }

        return Result.PLAYER
    }

    override fun toString(): String {
        return "$nick: $playerScore/${
            playerHand.toString().removePrefix("[").removeSuffix("]").replace(", ", ",")
        } vs dealer: $dealerScore/${
            dealerHand.toString().removePrefix("[").removeSuffix("]").replace(", ", ",")
        }"
    }

    fun asStart() = StartedGameOfBlackjack(playerHand = playerHand)
    fun play(action: Action): GameOfBlackjack {
        if (action.isDrawNewCard) {
            playerHand.add(deckOfCards.takeCard())
        }

        return this
    }

    private enum class Result { PLAYER, DEALER }
}
