package com.gitlab.jactor.blackjack.compose.display

import androidx.compose.desktop.ui.tooling.preview.Preview
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
import com.gitlab.jactor.blackjack.compose.ApplicationConfiguration
import com.gitlab.jactor.blackjack.compose.dto.Action
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
import kotlin.system.exitProcess

private val ARRANGE_5DP_SPACING = Arrangement.spacedBy(5.dp)
private val ARRANGE_15DP_SPACING = Arrangement.spacedBy(15.dp)
private val ARRANGE_50DP_SPACING = Arrangement.spacedBy(50.dp)

@Composable
@Preview
internal fun composeBlackjack(playerName: PlayerName = PlayerName("Tor Egil"), runScope: MainCoroutineDispatcher = Dispatchers.Main) {
    var blackjackState: BlackjackState? by remember { mutableStateOf(null) }
    ApplicationConfiguration.loadBlackjackState(runScope) { loadedState: BlackjackState -> blackjackState = loadedState }

    MaterialTheme {
        var gameOfBlackjack: GameOfBlackjack? by remember { mutableStateOf(null) }

        Column(modifier = Modifier.fillMaxSize().padding(15.dp), verticalArrangement = ARRANGE_5DP_SPACING) {
            Row(modifier = Modifier.align(Alignment.CenterHorizontally), horizontalArrangement = ARRANGE_5DP_SPACING) {
                Text("Hi ${playerName.capitalized}! Magnus challenge you to a game of Blackjack.")
            }

            Row(modifier = Modifier.align(Alignment.CenterHorizontally), horizontalArrangement = ARRANGE_5DP_SPACING) {
                composePlayAutomaticAndManualButtons(blackjackState, runScope, playerName) { played: GameOfBlackjack? ->
                    gameOfBlackjack = played
                }
            }

            gameOfBlackjack?.let {
                composeGameOfBlackjack(it, playerName)

                Row(modifier = Modifier.align(Alignment.CenterHorizontally), horizontalArrangement = ARRANGE_5DP_SPACING) {
                    if (it.status.isGameCompleted) {
                        displayExitAndRetryButtons(runScope, gameOfBlackjack, blackjackState, playerName) { played: GameOfBlackjack? ->
                            gameOfBlackjack = played
                        }
                    } else {
                        composeHitMeAndStayButtons(runScope, blackjackState, playerName) { played: GameOfBlackjack? ->
                            gameOfBlackjack = played
                        }
                    }
                }
            } // end compose game of blackjack with buttons
        } // end column
    } // end material theme
}

@Composable
private fun composePlayAutomaticAndManualButtons(
    blackjackState: BlackjackState?,
    scope: MainCoroutineDispatcher,
    playerName: PlayerName,
    gameConsumer: (GameOfBlackjack?) -> Unit
) {
    PlayButton(
        enabled = blackjackState != null,
        text = "Play automatic game of blackjack",
        scope = scope,
        onClick = { blackjackState?.playAutomatic(playerName)!! },
        gameConsumer = gameConsumer
    )

    PlayButton(
        enabled = blackjackState != null,
        text = "Play manual game of blackjack",
        scope = scope,
        onClick = { blackjackState?.playManual(Action.START, playerName)!! },
        gameConsumer = gameConsumer
    )
}

@Composable
private fun displayExitAndRetryButtons(
    scope: MainCoroutineDispatcher,
    gameOfBlackjack: GameOfBlackjack?,
    blackjackState: BlackjackState?,
    playerName: PlayerName,
    gameConsumer: (GameOfBlackjack?) -> Unit
) {
    Button(onClick = { exitProcess(0) }) {
        Text("Exit game!")
    }

    PlayButton(
        text = "Retry game!",
        scope = scope,
        onClick = {
            when (gameOfBlackjack?.gameType!!) {
                GameType.AUTOMATIC -> blackjackState?.playAutomatic(playerName)!!
                GameType.MANUAL -> blackjackState?.playManual(Action.START, playerName)!!
            }
        }, gameConsumer = gameConsumer
    )
}

@Composable
private fun composeHitMeAndStayButtons(
    scope: MainCoroutineDispatcher,
    blackjackState: BlackjackState?,
    playerName: PlayerName,
    gameConsumer: (GameOfBlackjack?) -> Unit
) {
    PlayButton(
        text = "Hit me!",
        scope = scope,
        onClick = {
            blackjackState?.playManual(
                Action.HIT,
                playerName
            )!!
        }, gameConsumer = gameConsumer
    )
    PlayButton(
        text = "I stay!",
        scope = scope,
        onClick = {
            blackjackState?.playManual(
                Action.END,
                playerName
            )!!
        }, gameConsumer = gameConsumer
    )
}

@Composable
private fun PlayButton(
    enabled: Boolean = true,
    text: String,
    scope: MainCoroutineDispatcher,
    onClick: () -> GameOfBlackjack,
    gameConsumer: (GameOfBlackjack?) -> Unit
) {

    Button(enabled = enabled, onClick = {
        CoroutineScope(Dispatchers.IO).launch {
            val game = onClick.invoke()
            withContext(scope) { gameConsumer.invoke(game) }
        }
    }) {
        Text(text)
    }
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
