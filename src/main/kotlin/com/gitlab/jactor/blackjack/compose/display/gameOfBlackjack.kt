package com.gitlab.jactor.blackjack.compose.display

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
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
private val ARRANGE_15DP_SPACING = Arrangement.spacedBy(15.dp)
private val ARRANGE_50DP_SPACING = Arrangement.spacedBy(50.dp)

@Composable
@Preview
internal fun composeBlackjack(playerName: PlayerName = PlayerName("Tor Egil"), scope: MainCoroutineDispatcher = Dispatchers.Main) {
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

                    if (gameOfBlackjack?.status?.isGameCompleted == true) {
                        Row(modifier = Modifier.align(Alignment.CenterHorizontally), horizontalArrangement = ARRANGE_5DP_SPACING) {
                            Button(
                                onClick = {
                                    println("i am outtahere...")
                                }
                            ) {
                                Text("Exit")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun composeGameOfBlackjack(gameOfBlackjack: GameOfBlackjack) {
    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = ARRANGE_5DP_SPACING) {
        Row(modifier = Modifier.align(Alignment.CenterHorizontally), horizontalArrangement = ARRANGE_50DP_SPACING) {
            Text("Dealer - ${gameOfBlackjack.status.dealerScore}")
        }

        Row(modifier = Modifier.align(Alignment.CenterHorizontally), horizontalArrangement = ARRANGE_15DP_SPACING) {
            val imageModifier = Modifier
                .height(95.dp)
                .width(70.dp)
                .align(alignment = Alignment.CenterVertically)
                .shadow(8.dp)

            gameOfBlackjack.dealerHand.forEach {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(
                        painter = painterResource(it.imageFileName),
                        contentDescription = it.text,
                        modifier = imageModifier,
                        contentScale = ContentScale.Fit,
                    )

                    Text(text = it.suit.name.lowercase(), color = withColor(it.color))
                    Text(text = it.face.name.lowercase(), color = withColor(it.color))
                }
            }
        }

        Row(modifier = Modifier.align(Alignment.CenterHorizontally), horizontalArrangement = ARRANGE_5DP_SPACING) {
            Text(text = "Player - ${gameOfBlackjack.status.playerScore}")
        }

        Row(modifier = Modifier.align(Alignment.CenterHorizontally), horizontalArrangement = ARRANGE_15DP_SPACING) {
            val imageModifier = Modifier
                .height(95.dp)
                .width(70.dp)
                .align(alignment = Alignment.CenterVertically)
                .shadow(8.dp)

            gameOfBlackjack.playerHand.forEach {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(
                        painter = painterResource(it.imageFileName),
                        contentDescription = it.text,
                        modifier = imageModifier,
                        contentScale = ContentScale.Fit,
                    )

                    Text(text = it.suit.name.lowercase(), color = withColor(it.color))
                    Text(text = it.face.name.lowercase(), color = withColor(it.color))
                }
            }
        }

        Row(modifier = Modifier.align(Alignment.CenterHorizontally), horizontalArrangement = ARRANGE_5DP_SPACING) {
            Text("${if (gameOfBlackjack.isAutomaticGame()) "Spillets" else "Rundens"} resultat:")
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

private fun withColor(color: Card.Color): Color {
    return when (color) {
        Card.Color.BLACK -> Color.Black
        Card.Color.RED -> Color.Red
    }
}
