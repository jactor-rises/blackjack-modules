package com.gitlab.jactor.blackjack.model

import com.gitlab.jactor.blackjack.dto.GameOfBlackjackDto
import com.gitlab.jactor.blackjack.dto.ResultDto

data class GameOfBlackjack(val deckOfCards: DeckOfCards, val nick: String, val isAutomaticGame: Boolean = true) {
    internal val playerHand: MutableList<Card> = deckOfCards.take(noOfCards = 2)
    internal val dealerHand: MutableList<Card> = deckOfCards.take(noOfCards = 2)

    private val dealerScore: Int get() = sumOf(dealerHand)
    private val playerScore: Int get() = sumOf(playerHand)
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

    private fun sumOf(cards: List<Card>): Int {
        if (isAutomaticGame) {
            return cards.sumOf { fetchValue(it.face) }
        }

        val allCards: MutableList<Card> = ArrayList(cards)
        val onlyAces: MutableList<Card> = ArrayList()

        cards.forEach {
            if (it.isAce) {
                onlyAces.add(it)
                allCards.remove(it)
            }
        }

        return calculateTotalSum(allCards.sumOf { fetchValue(it.face) }, onlyAces)
    }

    private fun fetchValue(face: Face, isAceFullScore: Boolean = true): Int {
        if (face.isAce) {
            return if (isAceFullScore) Value.ACE_FULL else Value.ACE_MIN
        } else if (face.value.matches("\\d+".toRegex())) {
            return face.value.toInt()
        }

        return Value.FACE_CARD // King, Queen, Jack
    }

    private fun calculateTotalSum(sumOfCardsNotAces: Int, listOfAces: List<Card>): Int {
        var totalSum = sumOfCardsNotAces

        listOfAces.forEach {
            totalSum += fetchValue(face = it.face, isAceFullScore = (totalSum + Value.ACE_FULL) < Value.BLACKJACK)
        }

        return totalSum
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

    fun asStart() = StartedGameOfBlackjack(playerHand = playerHand)
    fun play(action: Action): GameOfBlackjack {
        if (action.isDrawNewCard) {
            playerHand.add(deckOfCards.takeCard())
        }

        return this
    }

    override fun toString(): String {
        return "$nick: $playerScore/${
            playerHand.toString().removePrefix("[").removeSuffix("]").replace(", ", ",")
        } vs dealer: $dealerScore/${
            dealerHand.toString().removePrefix("[").removeSuffix("]").replace(", ", ",")
        }"
    }

    private enum class Result { PLAYER, DEALER }
}
