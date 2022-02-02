package com.gitlab.jactor.blackjack.model

import com.gitlab.jactor.blackjack.dto.GameStatus
import com.gitlab.jactor.blackjack.model.Face.*
import com.gitlab.jactor.blackjack.model.Suit.*
import com.gitlab.jactor.blackjack.model.TestUtil.aDeckOfCardsStartingWith
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

@DisplayName("A com.gitlab.jactor.blackjack.model.GameOfBlackjack")
internal class GameOfBlackjackTest {

    @Test
    fun `should have the hand of the player as well as for 'Magnus' when the game is created`() {
        val gameOfBlackjack = GameOfBlackjack(
            nick = "Tor Egil",
            deckOfCards = aDeckOfCardsStartingWith(Card(CLUBS, ACE), Card(DIAMONDS, KING), Card(HEARTS, QUEEN), Card(DIAMONDS, SEVEN))
        )

        assertAll(
            {
                assertThat(gameOfBlackjack.playerHand).`as`("Player hand").isEqualTo(
                    listOf(Card(CLUBS, ACE), Card(DIAMONDS, KING))
                )
            }, {
                assertThat(gameOfBlackjack.dealerHand).`as`("Dealer (aka. Magnus) hand").isEqualTo(
                    listOf(Card(HEARTS, QUEEN), Card(DIAMONDS, SEVEN))
                )
            }
        )
    }

    @ParameterizedTest
    @ValueSource(strings = ["HA,CK", "DA,SQ", "SJ,DA"]) // Ace & King, Ace & Queen, and Jack & Ace
    fun `should award the win to the player when dealt cards that ends up with final score of 21`(firstCardsInDeck: String) {
        val resultDto = GameOfBlackjack(
            nick = "Tor Egil",
            deckOfCards = aDeckOfCardsStartingWith(firstCardsInDeck.split(","))
        ).completeGame().toDto().status

        assertAll(
            { assertThat(resultDto.playerScore).`as`("score from $firstCardsInDeck").isEqualTo(21) },
            { assertThat(resultDto.status).`as`("game status").isEqualTo(GameStatus.PLAYER_WINS) }
        )
    }

    @ParameterizedTest
    @ValueSource(
        strings = [
            "DA,C7,HA,CK", // Player: Ace, 7    - Dealer: Ace & King
            "H4,CK,DA,SQ", // Player: 4 & King  - Dealer: Ace & Queen
            "HA,CA,SJ,DA"  // Player: Ace & Ace - Dealer: Jack & Ace
        ]
    )
    fun `should award the win to the dealer when dealt cards that ends up with with final score of 21 and the player does not`(firstCardsInDeck: String) {
        val gameOfBlackjack = GameOfBlackjack(
            nick = "Tor Egil",
            deckOfCards = aDeckOfCardsStartingWith(firstCardsInDeck.split(","))
        ).completeGame()
        val statusDto = gameOfBlackjack.toDto().status

        assertAll(
            { assertThat(statusDto.dealerScore).`as`("dealer score ($gameOfBlackjack)").isEqualTo(21) },
            { assertThat(statusDto.playerScore).`as`("player score ($gameOfBlackjack)").isNotEqualTo(21) },
            { assertThat(statusDto.status).`as`("game status").isEqualTo(GameStatus.DEALER_WINS) }
        )
    }

    @Test
    fun `should award the win to the dealer if the player has end score greater than 21 after game start`() {
        val resultDto = GameOfBlackjack(
            nick = "Tor Egil",
            deckOfCards = aDeckOfCardsStartingWith("HA,CA,D9,C3".split(","))
        ).completeGame().toDto().status

        assertAll(
            { assertThat(resultDto.dealerScore).`as`("dealer score").isEqualTo(12) },
            { assertThat(resultDto.playerScore).`as`("player score").isEqualTo(22) },
            { assertThat(resultDto.status).`as`("status").isEqualTo(GameStatus.DEALER_WINS) }
        )
    }

    @Test
    fun `should take a card for the player as long as total score is less than 17`() {
        // player cards: 9 + 3 + 2 + 3 + 2 is final score as 17
        val statusDto = GameOfBlackjack(
            nick = "Tor Egil",
            deckOfCards = aDeckOfCardsStartingWith("D9,C3,H5,CK,D2,S3,H2".split(","))
        ).completeGame().toDto().status

        assertThat(statusDto.playerScore).isEqualTo(17)
    }

    @Test
    fun `should end game when the player gets a final score above 21`() {
        // player cards: 9 + 3 + 2 + K is final score as 24
        val statusDto = GameOfBlackjack(
            nick = "Tor Egil",
            deckOfCards = aDeckOfCardsStartingWith("D9,C3,H5,CK,D2,SK".split(","))
        ).completeGame().toDto().status

        assertAll(
            { assertThat(statusDto.playerScore).`as`("player score").isEqualTo(24) },
            { assertThat(statusDto.status).`as`("status").isEqualTo(GameStatus.DEALER_WINS) }
        )
    }

    @ParameterizedTest
    @ValueSource(
        strings = [
            "D10,C7,HK,D5,S3",     // Player: 10, 7   (17) - Dealer: K, 5, 3    (18)
            "D5,C6,HQ,C5,D6,S3",   // Player: 5, 6, 6 (17) - Dealer: Q, 6, 3    (19)
            "D5,C7,HJ,C3,S5,S4,H3",// Player: 5, 7, 5 (17) - Dealer: J, 3, 4, 3 (20)
            "D5,C8,H9,H8,D4,S2,D2" // Player: 5, 8, 4 (17) - Dealer: 9, 8, 2, 2 (21)
        ]
    )
    fun `should take a card for the dealer as long as total score is less or equal to the players score`(firstCardsInDeck: String) {
        val gameOfBlackjack = GameOfBlackjack(
            nick = "Tor Egil",
            deckOfCards = aDeckOfCardsStartingWith(firstCardsInDeck.split(","))
        ).completeGame()
        val statusDto = gameOfBlackjack.toDto().status

        assertAll(
            { assertThat(statusDto.playerScore).`as`("player score ($gameOfBlackjack)").isEqualTo(17) },
            { assertThat(statusDto.dealerScore).`as`("dealer score ($gameOfBlackjack)").isGreaterThan(17) },
            { assertThat(statusDto.status).`as`("status ($gameOfBlackjack)").isEqualTo(GameStatus.DEALER_WINS) }
        )
    }

    @Test
    fun `should have dealer (Magnus) as winner`() {
        val gameOfBlackjack = GameOfBlackjack(
            nick = "Tor Egil",
            deckOfCards = aDeckOfCardsStartingWith("D5,DK,DA,D8,SA".split(","))
        ).completeGame().logResult()
        val statusDto = gameOfBlackjack.toDto().status

        assertAll(
            { assertThat(statusDto.dealerScore).`as`("dealer score ($gameOfBlackjack)").isEqualTo(19) },
            { assertThat(statusDto.playerScore).`as`("player score ($gameOfBlackjack)").isEqualTo(26) },
            { assertThat(statusDto.status).`as`("status ($gameOfBlackjack)").isEqualTo(GameStatus.DEALER_WINS) }
        )
    }

    @Test
    fun `should count aces as one when manual game and total value of cards are greater than 21`() {
        // DA, SA CA og HA (ruter, spar, kløver og hjerter ess) skal ha sum 14
        val gameOfBlackjack = GameOfBlackjack(
            nick = "Tor Egil",
            deckOfCards = aDeckOfCardsStartingWith("DA,SA,DJ,DK,CA,HA".split(",")),
            isAutomaticGame = false
        ).play(Action(isDrawNewCard = true)).play(Action(isDrawNewCard = true))
        val statusDto = gameOfBlackjack.toDto().status

        assertAll(
            { assertThat(statusDto.playerScore).`as`("player score (${gameOfBlackjack.playerHand})").isEqualTo(14) },
        )
    }
}
