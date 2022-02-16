package com.gitlab.jactor.blackjack.compose.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.gitlab.jactor.blackjack.compose.dto.Action
import com.gitlab.jactor.blackjack.compose.model.Card
import com.gitlab.jactor.blackjack.compose.model.GameOfBlackjack
import com.gitlab.jactor.blackjack.compose.model.GameOption
import com.gitlab.jactor.blackjack.compose.model.GameStatus
import com.gitlab.jactor.blackjack.compose.model.GameType
import com.gitlab.jactor.blackjack.compose.model.PlayerName
import com.gitlab.jactor.blackjack.compose.state.BlackjackState

private val ARRANGE_15DP_SPACING = Arrangement.spacedBy(15.dp)

@Composable
fun GameOfBlackjackUI(
    gameOfBlackjack: GameOfBlackjack,
    playerName: PlayerName,
    blackjackState: BlackjackState?,
    newGameOption: (GameOption) -> Unit
) {
    MaterialTheme {
        Column(modifier = Modifier.fillMaxSize().padding(10.dp), verticalArrangement = ARRANGE_5_DP_SPACING) {
            composeGameOfBlackjack(gameOfBlackjack, playerName)

            Row(modifier = Modifier.align(Alignment.CenterHorizontally), horizontalArrangement = ARRANGE_5_DP_SPACING) {
                if (gameOfBlackjack.status.isGameCompleted) {
                    Button(onClick = { newGameOption.invoke(GameOption.QUIT) }) { Text("Exit game!") }
                    Button(onClick = {
                        when (gameOfBlackjack.gameType) {
                            GameType.AUTOMATIC -> blackjackState?.playAutomatic(playerName)!!
                            GameType.MANUAL -> blackjackState?.playManual(Action.START, playerName)!!
                        }
                    }) {
                        Text("Retry game!")
                    }
                } else {
                    Button(onClick = { blackjackState?.playManual(Action.HIT, playerName) }) { Text("Hit me!") }
                    Button(onClick = { blackjackState?.playManual(Action.END, playerName) }) { Text("I stay!") }
                }
            }
        }
    }
}

@Composable
private fun composeGameOfBlackjack(gameOfBlackjack: GameOfBlackjack, playerName: PlayerName) {
    Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = ARRANGE_5_DP_SPACING) {
        Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
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

        Row(modifier = Modifier.align(Alignment.CenterHorizontally), horizontalArrangement = ARRANGE_5_DP_SPACING) {
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

        if (gameOfBlackjack.status.isGameCompleted) {
            Row(modifier = Modifier.align(Alignment.CenterHorizontally), horizontalArrangement = ARRANGE_5_DP_SPACING) {
                Text(
                    text = gameOfBlackjack.displayWinner(playerName),
                    color = when (gameOfBlackjack.status.fetchResultOfGame()) {
                        GameStatus.DEALER_WINS -> Color.DarkGray
                        GameStatus.PLAYER_WINS -> Color.Blue
                    }
                )
            }
        }
    }
}

private fun withColor(color: Card.Color): Color {
    return when (color) {
        Card.Color.BLACK -> Color.Black
        Card.Color.RED -> Color.Red
    }
}
