package com.gitlab.jactor.blackjack.model

import com.gitlab.jactor.blackjack.dto.GameOfBlackjackDto
import com.gitlab.jactor.blackjack.dto.ResultDto

data class GameOfBlackjack(val deckOfCards: DeckOfCards, val nick: String) {
    internal val playerHand: MutableList<Card> = deckOfCards.take(2)
    internal val dealerHand: MutableList<Card> = deckOfCards.take(2)

    fun completeGame(): GameOfBlackjack {
        return this
    }

    fun toDto() = GameOfBlackjackDto(
            nickOfPlayer = nick,
            dealerHand = dealerHand.map { it.toDto() },
            playerHand = playerHand.map { it.toDto() },
            resultat = initResultDto()
        )

    private fun initResultDto(): ResultDto {
        val dealerScore = dealerHand.sumOf { fetchValue(it.face) }
        val playerScore = playerHand.sumOf { fetchValue(it.face) }
        val winner = when(fetchResult(dealerScore, playerScore)) {
            Result.PLAYER -> nick
            Result.DEALER -> "Magnus"
        }

        return ResultDto(
            winner = winner,
            dealerScore = dealerScore,
            playerScore = playerScore
        )
    }

    private fun fetchValue(face: Face): Int {
        if (Face.ACE == face) {
            return 11
        } else if (VALUED_TEN.contains(face)) {
            return 10
        }

        return face.value.toInt()
    }

    private fun fetchResult(dealerScore: Int, playerScore: Int): Result {
        if (playerScore == 21) {
            return Result.PLAYER
        }

        if (dealerScore == 21) {
            return Result.DEALER
        }

        return Result.PLAYER
    }

    companion object {
        private val VALUED_TEN = setOf(Face.JACK, Face.QUEEN, Face.KING)
    }

    private enum class Result { PLAYER, DEALER }
}
