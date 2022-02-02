package com.gitlab.jactor.blackjack.model

import com.gitlab.jactor.blackjack.dto.GameOfBlackjackDto
import com.gitlab.jactor.blackjack.dto.GameStatus
import com.gitlab.jactor.blackjack.dto.StatusDto

data class GameOfBlackjack(val deckOfCards: DeckOfCards, val nick: String, val isAutomaticGame: Boolean = true) {
    internal val playerHand: MutableList<Card> = deckOfCards.take(noOfCards = 2)
    internal val dealerHand: MutableList<Card> = deckOfCards.take(noOfCards = 2)

    private val dealerScore: Int get() = sumOf(dealerHand)
    private val playerScore: Int get() = sumOf(playerHand)

    fun completeGame(): GameOfBlackjack {
        if (playerScore >= Value.BLACKJACK_21 || dealerScore >= Value.BLACKJACK_21) {
            return this
        }

        while (playerScore < Value.PLAYER_MINIMUM_17) {
            playerHand.add(deckOfCards.takeCard())
        }

        val playerFinalScore = playerScore

        if (playerFinalScore < Value.BLACKJACK_21) {
            while (dealerScore <= playerFinalScore) {
                dealerHand.add(deckOfCards.takeCard())
            }
        }

        return this
    }

    fun logResult(): GameOfBlackjack {
        println(
            """
                Vinner: $nick
                
                Magnus | $dealerScore | ${dealerHandAsString()}
                $nick | $playerScore | ${playerHandAsString()}
            """.trimIndent()
        )

        return this
    }

    fun toDto() = GameOfBlackjackDto(
        nickOfPlayer = nick,
        dealerHand = dealerHand.map { it.toDto() },
        playerHand = playerHand.map { it.toDto() },
        status = StatusDto(
            status = GameStatus.valueOf(fetchState().name),
            dealerScore = dealerScore,
            playerScore = playerScore,
            isGameCompleted = isGameCompleted()
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
            return if (isAceFullScore) Value.ACE_FULL_11 else Value.ACE_MIN_1
        } else if (face.value.matches("\\d+".toRegex())) {
            return face.value.toInt()
        }

        return Value.FACE_CARD_10 // King, Queen, Jack
    }

    private fun calculateTotalSum(sumOfCardsNotAces: Int, listOfAces: List<Card>): Int {
        var totalSum = sumOfCardsNotAces

        listOfAces.forEach {
            totalSum += fetchValue(face = it.face, isAceFullScore = (totalSum + Value.ACE_FULL_11) <= Value.BLACKJACK_21)
        }

        return totalSum
    }

    private fun fetchState(): State {
        val dealerFinalScore = dealerScore
        val playerFinalScore = playerScore

        if (playerFinalScore == Value.BLACKJACK_21) {
            return State.PLAYER_WINS
        }

        if (dealerFinalScore == Value.BLACKJACK_21) {
            return State.DEALER_WINS
        }

        if (playerFinalScore > Value.BLACKJACK_21) {
            return State.DEALER_WINS
        }

        if (dealerFinalScore in (playerFinalScore + 1) until Value.BLACKJACK_21) {
            return State.DEALER_WINS
        }

        return State.NOT_CONCLUDED
    }

    fun play(action: Action): GameOfBlackjack {
        if (action.isDrawNewCard) {
            playerHand.add(deckOfCards.takeCard())
        }

        return this
    }

    private fun isGameCompleted(): Boolean {
        return playerScore >= Value.BLACKJACK_21 || dealerScore >= Value.BLACKJACK_21
    }

    override fun toString(): String {
        return "$nick: $playerScore/${playerHandAsString()} vs dealer: $dealerScore/${dealerHandAsString()}"
    }

    private fun playerHandAsString() = playerHand.toString().removePrefix("[").removeSuffix("]").replace(", ", ",")
    private fun dealerHandAsString() = dealerHand.toString().removePrefix("[").removeSuffix("]").replace(", ", ",")

    enum class State { PLAYER_WINS, DEALER_WINS, NOT_CONCLUDED }
}
