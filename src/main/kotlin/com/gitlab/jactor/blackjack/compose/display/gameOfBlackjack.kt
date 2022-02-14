package com.gitlab.jactor.blackjack.compose.display

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.gitlab.jactor.blackjack.compose.ApplicationConfiguration
import com.gitlab.jactor.blackjack.compose.dto.Action
import com.gitlab.jactor.blackjack.compose.model.Card
import com.gitlab.jactor.blackjack.compose.model.GameOfBlackjack
import com.gitlab.jactor.blackjack.compose.model.GameStatus
import com.gitlab.jactor.blackjack.compose.model.GameType
import com.gitlab.jactor.blackjack.compose.model.PlayerName
import com.gitlab.jactor.blackjack.compose.state.BlackjackState
import com.gitlab.jactor.blackjack.compose.state.Lce
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainCoroutineDispatcher
import kotlin.system.exitProcess

private val ARRANGE_5DP_SPACING = Arrangement.spacedBy(5.dp)
private val ARRANGE_15DP_SPACING = Arrangement.spacedBy(15.dp)
private val ARRANGE_50DP_SPACING = Arrangement.spacedBy(50.dp)

@Composable
@Preview
internal fun composeBlackjack(playerName: PlayerName = PlayerName("Tor Egil"), runScope: MainCoroutineDispatcher = Dispatchers.Main) {
    var blackjackState: BlackjackState? by remember { mutableStateOf(null) }
    var gameState: Lce<GameOfBlackjack> by remember { mutableStateOf(Lce.Loading as Lce<GameOfBlackjack>) }

    ApplicationConfiguration.loadBlackjackState(
        runScope = runScope,
        blackjackStateConsumer = { loadedState: BlackjackState -> blackjackState = loadedState },
        gameStateConsumer = { newGameState: Lce<GameOfBlackjack> -> gameState = newGameState }
    )

    MaterialTheme {
        Column(modifier = Modifier.fillMaxSize().padding(15.dp), verticalArrangement = ARRANGE_5DP_SPACING) {
            Row(modifier = Modifier.align(Alignment.CenterHorizontally), horizontalArrangement = ARRANGE_5DP_SPACING) {
                Text("Hi ${playerName.capitalized}! Magnus challenge you to a game of Blackjack.")
            }

            Row(modifier = Modifier.align(Alignment.CenterHorizontally), horizontalArrangement = ARRANGE_5DP_SPACING) {
                composePlayAutomaticAndManualButtons(blackjackState, playerName)
            }

            when (gameState) {
                is Lce.Loading -> LoadingUI(loadingContent = Lce.Loading.loadingContet)
                is Lce.Error -> ErrorUI(gameState)
                is Lce.Content -> {
                    @Suppress("UNCHECKED_CAST") val gameOfBlackjack = (gameState as Lce.Content<GameOfBlackjack>).data
                    composeGameOfBlackjack(gameOfBlackjack, playerName)

                    Row(modifier = Modifier.align(Alignment.CenterHorizontally), horizontalArrangement = ARRANGE_5DP_SPACING) {
                        if (gameOfBlackjack.status.isGameCompleted) {
                            Button(onClick = { exitProcess(0) }) { Text("Exit game!") }
                            PlayButton(text = "Retry game!") {
                                when (gameOfBlackjack.gameType) {
                                    GameType.AUTOMATIC -> blackjackState?.playAutomatic(playerName)!!
                                    GameType.MANUAL -> blackjackState?.playManual(Action.START, playerName)!!
                                }
                            }
                        } else {
                            PlayButton(text = "Hit me!", onClick = { blackjackState?.playManual(Action.HIT, playerName) })
                            PlayButton(text = "I stay!", onClick = { blackjackState?.playManual(Action.END, playerName) })
                        }
                    }
                } // end compose game of blackjack with buttons
            } // end column
        } // end material theme
    }
}

@Composable
private fun composePlayAutomaticAndManualButtons(blackjackState: BlackjackState?, playerName: PlayerName) {
    PlayButton(enabled = blackjackState != null, text = "Play automatic game of blackjack", onClick = { blackjackState?.playAutomatic(playerName) })
    PlayButton(
        enabled = blackjackState != null,
        text = "Play manual game of blackjack",
        onClick = { blackjackState?.playManual(Action.START, playerName) }
    )
}

@Composable
private fun PlayButton(enabled: Boolean = true, text: String, onClick: () -> Unit) {
    Button(enabled = enabled, onClick = { onClick.invoke() }) {
        Text(text)
    }
}

@Composable
fun LoadingUI(loadingContent: Boolean) {
    if (loadingContent) {
        Box(modifier = Modifier.fillMaxSize()) {
            CircularProgressIndicator(

                modifier = Modifier
                    .align(alignment = Alignment.Center)
                    .defaultMinSize(minWidth = 96.dp, minHeight = 96.dp)
            )
        }
    }
}

@Composable
private fun ErrorUI(gameState: Lce<GameOfBlackjack>) {
    val fail = (gameState as Lce.Error)
    val cause = fail.error::class.simpleName
    val message = fail.error.message

    Text(text = "Something fishy happened! $cause: $message", textAlign = TextAlign.Center, color = Color.Red)
}

@Composable
private fun composeGameOfBlackjack(gameOfBlackjack: GameOfBlackjack, playerName: PlayerName) {
    Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = ARRANGE_5DP_SPACING) {
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

        if (gameOfBlackjack.status.isGameCompleted) {
            Row(modifier = Modifier.align(Alignment.CenterHorizontally), horizontalArrangement = ARRANGE_5DP_SPACING) {
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
