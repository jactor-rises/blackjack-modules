package com.gitlab.jactor.blackjack.model

import com.gitlab.jactor.blackjack.dto.GameOfBlackjackDto
import com.gitlab.jactor.blackjack.dto.ResultDto

data class GameOfBlackjack(val deckOfCards: DeckOfCards, val nick: String) {
    internal val playerHand: MutableList<Card> = deckOfCards.take(noOfCards = 2)
    internal val dealerHand: MutableList<Card> = deckOfCards.take(noOfCards = 2)
    private val dealerScore: Int get() = dealerHand.sumOf { fetchValue(it.face) }
    private val playerScore: Int get() = playerHand.sumOf { fetchValue(it.face) }

    fun completeGame(): GameOfBlackjack {
        if (dealerScore > 21) { // will happen if the dealer get 2 aces
            return this
        }

        while (playerScore < 17) {
            playerHand.add(deckOfCards.takeCard())
        }

        while (playerScore < 22 && playerScore >= dealerScore) {
            dealerHand.add(deckOfCards.takeCard())
        }

        return this
    }

    fun toDto() = GameOfBlackjackDto(
        nickOfPlayer = nick,
        dealerHand = dealerHand.map { it.toDto() },
        playerHand = playerHand.map { it.toDto() },
        resultat = initResultDto()
    )

    private fun initResultDto(): ResultDto {
        val winner = when (fetchResult()) {
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
        } else if (face.value.matches(Regex("\\d+"))) {
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

        if (dealerFinalScore < 21 && dealerFinalScore > playerFinalScore) {
            return Result.DEALER
        }

        return Result.PLAYER
    }

    override fun toString(): String {
        return "$nick: $playerScore/$playerHand vs dealer: $dealerScore/$dealerHand"
    }

    private enum class Result { PLAYER, DEALER }
}
