package com.gitlab.jactor.blackjack.model

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
    @ValueSource(strings = ["HA,CK", "DA,SQ", "SJ,DA"]) // Hearts/Ace&Clubs/King, Diamonds/Ace&Spades/Queen, and Spades/Jack&Diamonds/Ace
    fun `player should win when dealt cards that ends with the score 21`(firstCardsInDeck: String) {
        val resultDto = GameOfBlackjack(
            nick = "Tor Egil",
            deckOfCards = aDeckOfCardsStartingWith(firstCardsInDeck.split(","))
        ).completeGame().toDto().resultat

        assertAll(
            { assertThat(resultDto.playerScore).`as`("score from $firstCardsInDeck").isEqualTo(21) },
            { assertThat(resultDto.winner).`as`("game winner").isEqualTo("Tor Egil") }
        )
    }

    @ParameterizedTest
    @ValueSource(
        strings = [
            "DA,C7,HA,CK", // Player: Diamonds/Ace&Clubs/7 - Dealer: Hearts/Ace&Clubs/King
            "H4,CK,DA,SQ", // Player: Hearts/4&Clubs/King  - Dealer: Diamonds/Ace&Spades/Queen
            "HA,CA,SJ,DA"  // Player: Hearts/Ace&Clubs/Ace - Dealer: Spades/Jack&Diamonds/Ace
        ]
    )
    fun `dealer should win when dealt cards that ends with 21 but the player does not`(firstCardsInDeck: String) {
        val resultDto = GameOfBlackjack(
            nick = "Tor Egil",
            deckOfCards = aDeckOfCardsStartingWith(firstCardsInDeck.split(","))
        ).completeGame().toDto().resultat

        assertAll(
            { assertThat(resultDto.dealerScore).`as`("dealer score from $firstCardsInDeck").isEqualTo(21) },
            { assertThat(resultDto.playerScore).`as`("player score from $firstCardsInDeck").isNotEqualTo(21) },
            { assertThat(resultDto.winner).`as`("game winner").isEqualTo("Magnus") }
        )
    }
}
