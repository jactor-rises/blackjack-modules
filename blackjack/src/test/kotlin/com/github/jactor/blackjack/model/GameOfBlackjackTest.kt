package com.github.jactor.blackjack.model

import com.github.jactor.blackjack.dto.GameStatus
import com.github.jactor.blackjack.model.Face.*
import com.github.jactor.blackjack.model.Suit.*
import com.github.jactor.blackjack.model.TestUtil.aDeckOfCardsStartingWith
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

@DisplayName("A com.github.jactor.blackjack.model.GameOfBlackjack")
internal class GameOfBlackjackTest {

    @Test
    fun `should have the hand of the player as well as for 'Banken' when the game is created`() {
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
                assertThat(gameOfBlackjack.dealerHand).`as`("Dealer (aka. Banken) hand").isEqualTo(
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
        ).completeGame().toDto().status!!

        assertAll(
            { assertThat(resultDto.playerScore).`as`("score from $firstCardsInDeck").isEqualTo(21) },
            { assertThat(resultDto.result).`as`("game status").isEqualTo(GameStatus.PLAYER_WINS) }
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
        val statusDto = gameOfBlackjack.toDto().status!!

        assertAll(
            { assertThat(statusDto.dealerScore).`as`("dealer score ($gameOfBlackjack)").isEqualTo(21) },
            { assertThat(statusDto.playerScore).`as`("player score ($gameOfBlackjack)").isNotEqualTo(21) },
            { assertThat(statusDto.result).`as`("game status").isEqualTo(GameStatus.DEALER_WINS) }
        )
    }

    @Test
    fun `should award the win to the dealer if the player has end score greater than 21 after game start`() {
        val resultDto = GameOfBlackjack(
            nick = "Tor Egil",
            deckOfCards = aDeckOfCardsStartingWith("HA,CA,D9,C3".split(","))
        ).completeGame().toDto().status!!

        assertAll(
            { assertThat(resultDto.dealerScore).`as`("dealer score").isEqualTo(12) },
            { assertThat(resultDto.playerScore).`as`("player score").isEqualTo(22) },
            { assertThat(resultDto.result).`as`("status").isEqualTo(GameStatus.DEALER_WINS) }
        )
    }

    @Test
    fun `should take a card for the player as long as total score is less than 17`() {
        // player cards: 9 + 3 + 2 + 3 + 2 is final score as 17
        val statusDto = GameOfBlackjack(
            nick = "Tor Egil",
            deckOfCards = aDeckOfCardsStartingWith("D9,C3,H5,CK,D2,S3,H2".split(","))
        ).completeGame().toDto().status!!

        assertThat(statusDto.playerScore).isEqualTo(17)
    }

    @Test
    fun `should end game when the player gets a final score above 21`() {
        // player cards: 9 + 3 + 2 + K is final score as 24
        val statusDto = GameOfBlackjack(
            nick = "Tor Egil",
            deckOfCards = aDeckOfCardsStartingWith("D9,C3,H5,CK,D2,SK".split(","))
        ).completeGame().toDto().status!!

        assertAll(
            { assertThat(statusDto.playerScore).`as`("player score").isEqualTo(24) },
            { assertThat(statusDto.result).`as`("status").isEqualTo(GameStatus.DEALER_WINS) }
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
        val statusDto = gameOfBlackjack.toDto().status!!

        assertAll(
            { assertThat(statusDto.playerScore).`as`("player score ($gameOfBlackjack)").isEqualTo(17) },
            { assertThat(statusDto.dealerScore).`as`("dealer score ($gameOfBlackjack)").isGreaterThan(17) },
            { assertThat(statusDto.result).`as`("status ($gameOfBlackjack)").isEqualTo(GameStatus.DEALER_WINS) }
        )
    }

    @Test
    fun `should have dealer (Banken) as winner`() {
        val gameOfBlackjack = GameOfBlackjack(
            nick = "Tor Egil",
            deckOfCards = aDeckOfCardsStartingWith("D5,DK,DA,D8,SA".split(","))
        ).completeGame().logResult()
        val statusDto = gameOfBlackjack.toDto().status!!

        assertAll(
            { assertThat(statusDto.dealerScore).`as`("dealer score ($gameOfBlackjack)").isEqualTo(19) },
            { assertThat(statusDto.playerScore).`as`("player score ($gameOfBlackjack)").isEqualTo(26) },
            { assertThat(statusDto.result).`as`("status ($gameOfBlackjack)").isEqualTo(GameStatus.DEALER_WINS) }
        )
    }

    @Test
    fun `should count aces as one when manual game and total value of cards are greater than 21`() {
        // DA, SA CA og HA (ruter, spar, kløver og hjerter ess) skal ha sum 14
        val gameOfBlackjack = GameOfBlackjack(
            nick = "Tor Egil",
            deckOfCards = aDeckOfCardsStartingWith("DA,SA,DJ,DK,CA,HA".split(",")),
            isManualGame = true
        ).play(Action.HIT).play(Action.HIT)
        val statusDto = gameOfBlackjack.toDto().status!!

        assertAll(
            { assertThat(statusDto.playerScore).`as`("player score (${gameOfBlackjack.playerHand})").isEqualTo(14) },
        )
    }

    @Test
    fun `should automatc complete game when player gets blackjack`() {
        val gameOfBlackjack = GameOfBlackjack(
            nick = "Tor Egil",
            deckOfCards = aDeckOfCardsStartingWith("D10,SA".split(",")),
            isManualGame = true
        )
        val statusDto = gameOfBlackjack.toDto().status!!

        assertThat(statusDto.isGameCompleted).`as`("$gameOfBlackjack should be complemeted automatic").isEqualTo(true)
    }

    @Test
    fun `should automatc complete game when dealer gets blackjack`() {
        val gameOfBlackjack = GameOfBlackjack(
            nick = "Tor Egil",
            deckOfCards = aDeckOfCardsStartingWith("D9,C8,HJ,HA".split(",")),
            isManualGame = true
        )
        val statusDto = gameOfBlackjack.toDto().status!!

        assertThat(statusDto.isGameCompleted).`as`("$gameOfBlackjack should be complemeted automatic").isEqualTo(true)
    }

    @ParameterizedTest
    @ValueSource(
        strings = [
            "D10,SK,HQ,C5,D6", // Player: 10, K, 6 (26) - Player busted
            "DK,C7,H10,H5,D7"  // Dealer: 10, 5, 7 (22) - Dealer busted (player 17)
        ]
    )
    fun `should automatic close game when dealer or player get blackjack or get busted`(firstCardsInDeck: String) {
        val gameOfBlackjack = GameOfBlackjack(
            nick = "Tor Egil",
            deckOfCards = aDeckOfCardsStartingWith(firstCardsInDeck.split(",")),
            isManualGame = true
        ).completeGame()

        val statusDto = gameOfBlackjack.toDto().status!!

        assertThat(statusDto.isGameCompleted).`as`("$gameOfBlackjack should be complemeted automatic").isEqualTo(true)
    }

    @Test
    fun `should award win to player when dealer is busted`() {
        // Banken   | 22 | DJ,H5,D7
        // Tor Egil | 19 | CJ,C9

        val statusDto = GameOfBlackjack(
            nick = "Tor Egil",
            deckOfCards = aDeckOfCardsStartingWith("CJ,C9,DJ,H5,D7".split(","))
        ).completeGame().toDto().status!!

        assertAll(
            { assertThat(statusDto.result).`as`("game status").isEqualTo(GameStatus.PLAYER_WINS) },
            { assertThat(statusDto.dealerScore).`as`("dealerScore").isEqualTo(22) },
            { assertThat(statusDto.playerScore).`as`("playerScore").isEqualTo(19) }
        )
    }

    @Test
    fun `should have an automatic game completed`() {
        // jactor  | 17 | S6,DA
        // Banken  | 20 | HQ,S10

        val statusDto = GameOfBlackjack(
            nick = "jactor",
            deckOfCards = aDeckOfCardsStartingWith("S6,DA,HQ,S10".split(","))
        ).completeGame().toDto().status!!

        assertAll(
            { assertThat(statusDto.result).`as`("game status").isEqualTo(GameStatus.DEALER_WINS) },
            { assertThat(statusDto.dealerScore).`as`("dealerScore").isEqualTo(20) },
            { assertThat(statusDto.playerScore).`as`("playerScore").isEqualTo(17) },
            { assertThat(statusDto.isGameCompleted).`as`("isGameCompleted").isTrue() }
        )
    }

    @Test
    fun `should complete game when action is END`() {
        // jactor  | 7 | S3,D4
        // Banken  | 4 | S2,C2

        val gameOfBlackjack = GameOfBlackjack(
            nick = "jactor",
            deckOfCards = aDeckOfCardsStartingWith("S3,D4,S2,C2".split(",")),
            isManualGame = true
        )

        assertAll(
            { assertThat(gameOfBlackjack.isGameCompleted()).`as`("Started game with low values").isEqualTo(false) },
            { assertThat(gameOfBlackjack.play(Action.END).isGameCompleted()).`as`("Ended game").isEqualTo(true) }
        )
    }
}
