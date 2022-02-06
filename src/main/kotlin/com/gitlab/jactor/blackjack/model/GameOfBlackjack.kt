package com.gitlab.jactor.blackjack.model

import com.gitlab.jactor.blackjack.dto.GameOfBlackjackDto
import com.gitlab.jactor.blackjack.dto.GameStatus
import com.gitlab.jactor.blackjack.dto.StatusDto

data class GameOfBlackjack(val deckOfCards: DeckOfCards, val nick: String, val isManualGame: Boolean = false) {
    internal val playerHand: MutableList<Card> = deckOfCards.take(noOfCards = 2)
    internal val dealerHand: MutableList<Card> = deckOfCards.take(noOfCards = 2)

    private val dealerScore: Int get() = sumOf(dealerHand)
    private val playerScore: Int get() = sumOf(playerHand)

    fun completeGame(): GameOfBlackjack {
        if (playerScore >= Value.BLACKJACK_21 || dealerScore >= Value.BLACKJACK_21) {
            return this
        }

        if (!isManualGame) {
            while (playerScore < Value.PLAYER_MINIMUM_17) {
                playerHand.add(deckOfCards.takeCard())
            }
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
        val dealerString = if (nick.length < 7) "Magnus".padEnd(7) else "Magnus".padEnd(nick.length)
        val playerString = if (nick.length < 7) nick.padEnd(7) else nick.padEnd(nick.length)

        println(
            """
                /=================================
                | ${fetchState()}
                +---------------------------------
                | $dealerString | $dealerScore | ${dealerHandAsString()}
                | $playerString | $playerScore | ${playerHandAsString()}
            """.trimIndent()
        )

        return this
    }

    fun toDto(action: Action?) = GameOfBlackjackDto(
        nickOfPlayer = nick,
        playerHand = playerHand.map { it.toDto() },
        dealerHand = dealerHand.map { it.toDto() },
        status = StatusDto(
            result = GameStatus.valueOf(fetchState().name),
            dealerScore = dealerScore,
            playerScore = playerScore,
            isGameCompleted = action == Action.END || isGameCompleted()
        )
    )

    fun toDto() = toDto(action = null)

    private fun sumOf(cards: List<Card>): Int {
        if (!isManualGame) {
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

        return State.PLAYER_WINS
    }

    fun play(action: Action?): GameOfBlackjack {
        if (action == Action.HIT) {
            playerHand.add(deckOfCards.takeCard())
        }

        return this
    }

    fun isGameCompleted() = !isManualGame || playerScore >= Value.BLACKJACK_21 || dealerScore >= Value.BLACKJACK_21
    fun isNotGameCompleted() = !isGameCompleted()

    override fun toString(): String {
        return "$nick: $playerScore/${playerHandAsString()} vs dealer: $dealerScore/${dealerHandAsString()}"
    }

    private fun playerHandAsString() = playerHand.toString().removePrefix("[").removeSuffix("]").replace(", ", ",")
    private fun dealerHandAsString() = dealerHand.toString().removePrefix("[").removeSuffix("]").replace(", ", ",")

    enum class State { PLAYER_WINS, DEALER_WINS }
}
