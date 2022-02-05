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
import com.gitlab.jactor.blackjack.compose.model.GameType
import com.gitlab.jactor.blackjack.compose.model.PlayerName
import com.gitlab.jactor.blackjack.compose.state.BlackjackState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
internal fun composeBlackjack(playerName: PlayerName) {

    MaterialTheme {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            val blackjackState = BlackjackState(Dispatchers.IO)
            var gameOfBlackjack: GameOfBlackjack? by remember { mutableStateOf(null) }

            Row {
                Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(5.dp)) {
                    Row(modifier = Modifier.align(Alignment.CenterHorizontally), horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                        Button(
                            onClick = {
                                CoroutineScope(Dispatchers.Main).launch {
                                    val game = blackjackState.play(GameType.AUTOMATIC, playerName)
                                    gameOfBlackjack = game
                                }
                            }
                        ) {
                            Text("Play automatic game of blackjack")
                        }

                        Button(
                            onClick = {
                                CoroutineScope(Dispatchers.Main).launch {
                                    val game = blackjackState.play(GameType.MANUAL, playerName)
                                    gameOfBlackjack = game
                                }
                            }
                        ) {
                            Text("Play manual game of blackjack")
                        }
                    }

                    gameOfBlackjack?.let {
                        Row(modifier = Modifier.align(Alignment.CenterHorizontally), horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                            Text("Dealer")

                            it.dealerHand.forEach {
                                Text(
                                    text = it.text,
                                    color = when (it.color) {
                                        Card.Color.BLACK -> Color.Black
                                        Card.Color.RED -> Color.Red
                                    }
                                )
                            }
                        }

                        Row(modifier = Modifier.align(Alignment.CenterHorizontally), horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                            Text(text = "Player")

                            it.playerHand.forEach {
                                Text(
                                    text = it.text,
                                    color = when (it.color) {
                                        Card.Color.BLACK -> Color.Black
                                        Card.Color.RED -> Color.Red
                                    }
                                )
                            }
                        }

                        Row(modifier = Modifier.align(Alignment.CenterHorizontally), horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                            Text("Resultat"); Text(it.displayWinner())
                        }
                    }
                }
            }
        }
    }
}
