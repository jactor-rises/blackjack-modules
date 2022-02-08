package com.gitlab.jactor.blackjack.compose.display

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.gitlab.jactor.blackjack.compose.model.Card
import com.gitlab.jactor.blackjack.compose.model.GameOfBlackjack
import com.gitlab.jactor.blackjack.compose.model.GameStatus
import com.gitlab.jactor.blackjack.compose.model.GameType
import com.gitlab.jactor.blackjack.compose.model.PlayerName
import com.gitlab.jactor.blackjack.compose.state.BlackjackState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainCoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private val ARRANGE_5DP_SPACING = Arrangement.spacedBy(5.dp)

@Composable
internal fun composeBlackjack(playerName: PlayerName, scope: MainCoroutineDispatcher) {
    var blackjackState: BlackjackState? by remember { mutableStateOf(null) }

    CoroutineScope(Dispatchers.Default).launch {
        val defaultBlackjackState = BlackjackState(Dispatchers.IO)
        withContext(scope) { blackjackState = defaultBlackjackState }
    }

    MaterialTheme {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            var gameOfBlackjack: GameOfBlackjack? by remember { mutableStateOf(null) }

            Row {
                Column(modifier = Modifier.fillMaxSize(), verticalArrangement = ARRANGE_5DP_SPACING) {
                    Row(modifier = Modifier.align(Alignment.CenterHorizontally), horizontalArrangement = ARRANGE_5DP_SPACING) {
                        Button(
                            onClick = {
                                CoroutineScope(Dispatchers.IO).launch {
                                    val game = blackjackState?.play(GameType.AUTOMATIC, playerName)
                                    withContext(scope) { gameOfBlackjack = game }
                                }
                            }
                        ) {
                            Text("Play automatic game of blackjack")
                        }

                        Button(
                            onClick = {
                                CoroutineScope(Dispatchers.IO).launch {
                                    val game = blackjackState?.play(GameType.MANUAL, playerName)
                                    withContext(scope) { gameOfBlackjack = game }
                                }
                            }
                        ) {
                            Text("Play manual game of blackjack")
                        }
                    }

                    gameOfBlackjack?.let {
                        composeGameOfBlackjack(it)
                    }
                }
            }
        }
    }
}

@Composable
private fun composeGameOfBlackjack(gameOfBlackjack: GameOfBlackjack) {
    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = ARRANGE_5DP_SPACING) {
        Row(modifier = Modifier.Companion.align(Alignment.CenterHorizontally), horizontalArrangement = ARRANGE_5DP_SPACING) {
            Text("Dealer - ${gameOfBlackjack.status.dealerScore}")
        }

        Row(modifier = Modifier.Companion.align(Alignment.CenterHorizontally), horizontalArrangement = ARRANGE_5DP_SPACING) {
            gameOfBlackjack.dealerHand.forEach {
                Text(
                    text = it.text,
                    color = when (it.color) {
                        Card.Color.BLACK -> Color.Black
                        Card.Color.RED -> Color.Red
                    }
                )
            }
        }

        Row(modifier = Modifier.Companion.align(Alignment.CenterHorizontally), horizontalArrangement = ARRANGE_5DP_SPACING) {
            Text(text = "Player - ${gameOfBlackjack.status.playerScore}")
        }

        Row(modifier = Modifier.Companion.align(Alignment.CenterHorizontally), horizontalArrangement = ARRANGE_5DP_SPACING) {
            gameOfBlackjack.playerHand.forEach {
                Text(
                    text = it.text,
                    color = when (it.color) {
                        Card.Color.BLACK -> Color.Black
                        Card.Color.RED -> Color.Red
                    }
                )
            }
        }

        Row(modifier = Modifier.Companion.align(Alignment.CenterHorizontally), horizontalArrangement = ARRANGE_5DP_SPACING) {
            Text("${if (gameOfBlackjack.isAutomsticGame()) "Spillets" else "Rundens"} resultat:")
            Text(
                text = gameOfBlackjack.displayWinner(),
                color = when (gameOfBlackjack.status.fetchResultOfGame()) {
                    GameStatus.DEALER_WINS -> Color.DarkGray
                    GameStatus.PLAYER_WINS -> Color.Blue
                }
            )
        }
    }
}
