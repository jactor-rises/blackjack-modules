package com.gitlab.jactor.blackjack.model

import com.gitlab.jactor.blackjack.model.Face.*
import com.gitlab.jactor.blackjack.model.Suit.*
import com.gitlab.jactor.blackjack.model.TestUtil.aDeckOfCardsStartingWith
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll

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

    @Test
    fun `player should win when `() {
    }
}
