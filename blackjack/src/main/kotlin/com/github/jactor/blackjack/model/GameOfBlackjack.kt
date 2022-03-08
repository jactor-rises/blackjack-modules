package com.github.jactor.blackjack.model

import com.github.jactor.blackjack.dto.GameOfBlackjackDto
import com.github.jactor.blackjack.dto.GameStatus
import com.github.jactor.blackjack.dto.GameType
import com.github.jactor.blackjack.dto.StatusDto

data class GameOfBlackjack(
    val deckOfCards: DeckOfCards,
    val nick: String,
    val isManualGame: Boolean = false,
    private var isGameCompleted: Boolean = false
) {
    internal val gameId: GameId
        get() = if (id != null) id!! else {
            id = GameId()
            id!!
        }

    internal val playerHand: MutableList<Card> = deckOfCards.take(noOfCards = 2)
    internal val dealerHand: MutableList<Card> = deckOfCards.take(noOfCards = 2)

    private val dealerScore: Int get() = sumOf(dealerHand)
    private val playerScore: Int get() = sumOf(playerHand)
    private var id: GameId? = null

    fun completeGame(endGame: Boolean = false): GameOfBlackjack {
        if (playerScore >= Value.BLACKJACK_21 || dealerScore >= Value.BLACKJACK_21) {
            isGameCompleted = true
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

        if (endGame) {
            isGameCompleted = true
        }

        return this
    }

    fun logResult(): GameOfBlackjack {
        if (isGameCompleted()) {
            val dealerString = if (nick.length < 7) "Banken".padEnd(7) else "Banken".padEnd(nick.length)
            val playerString = if (nick.length < 7) nick.padEnd(7) else nick.padEnd(nick.length)
            val dealerScore = this.dealerScore
            val playerScore = this.playerScore

            println(
                """
                    /=================================================
                    | ${displayWinnerOfGame(dealerScore, playerScore)}
                    +-------------------------------------------------
                    | $dealerString | ${dealerScore.atLeastTwoCharacters()} | ${dealerHandAsString()}
                    | $playerString | ${playerScore.atLeastTwoCharacters()} | ${playerHandAsString()}
                """.trimIndent()
            )
        }

        return this
    }

    private fun displayWinnerOfGame(dealerScore: Int, playerScore: Int): String {
        val state = fetchState()

        return when (state) {
            State.DEALER_WINS -> "${if (isBlackjack(dealerScore)) "Banken fikk blackjack" else displayWinner(state, dealerScore, playerScore)}!"
            State.PLAYER_WINS -> "${if (isBlackjack(playerScore)) "$nick fikk blackjack" else displayWinner(state, dealerScore, playerScore)}!"
        }
    }

    private fun displayWinner(state: State, dealerScore: Int, playerScore: Int): String {
        val winner = if (state == State.DEALER_WINS) "Banken" else nick

        if (dealerScore > 21 || playerScore > 21) {
            return "$winner vinner da ${if (state == State.DEALER_WINS) nick else "Banken"} kom over 21 poeng"
        }

        if (playerScore != dealerScore) {
            return "$winner vant med ${calculateDifference(state, dealerScore, playerScore)} poeng"
        }

        return "$winner vant spillet"
    }

    private fun calculateDifference(state: State, dealerScore: Int, playerScore: Int): Int = when (state) {
        State.DEALER_WINS -> dealerScore - playerScore
        State.PLAYER_WINS -> playerScore - dealerScore
    }

    private fun isBlackjack(score: Int) = score == 21

    fun toDto() = GameOfBlackjackDto(
        nickOfPlayer = nick,
        playerHand = playerHand.map { it.toDto() },
        dealerHand = dealerHand.map { it.toDto() },
        gameType = if (isManualGame) GameType.MANUAL else GameType.AUTOMATIC,
        gameId = gameId.id,
        status = StatusDto(
            result = GameStatus.valueOf(fetchState().name),
            dealerScore = dealerScore,
            playerScore = playerScore,
            isGameCompleted = isGameCompleted()
        )
    )

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

    fun play(action: Action): GameOfBlackjack {
        when (action) {
            Action.HIT -> playerHand.add(deckOfCards.takeCard())
            Action.START -> isGameCompleted = false
            Action.END -> completeGame(endGame = true)
        }

        return this
    }

    fun isGameCompleted() = !isManualGame || isGameCompleted || playerScore >= Value.BLACKJACK_21 || dealerScore >= Value.BLACKJACK_21
    fun isNotGameCompleted() = !isGameCompleted()

    override fun toString(): String {
        return "$nick: $playerScore/${playerHandAsString()} vs dealer: $dealerScore/${dealerHandAsString()}"
    }

    private fun playerHandAsString() = playerHand.toString().removePrefix("[").removeSuffix("]").replace(", ", ",")
    private fun dealerHandAsString() = dealerHand.toString().removePrefix("[").removeSuffix("]").replace(", ", ",")
    fun withGameId(): Boolean = id != null

    enum class State { PLAYER_WINS, DEALER_WINS }

    private fun Int.atLeastTwoCharacters(): String {
        return if (this < 10) "0$this" else this.toString()
    }
}
