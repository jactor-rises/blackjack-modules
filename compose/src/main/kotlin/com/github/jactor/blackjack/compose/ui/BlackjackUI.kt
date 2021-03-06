package com.github.jactor.blackjack.compose.ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Warning
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.github.jactor.blackjack.compose.model.ExceptionUtil
import com.github.jactor.blackjack.compose.model.GameOfBlackjack
import com.github.jactor.blackjack.compose.model.GameOption
import com.github.jactor.blackjack.compose.model.PlayerName
import com.github.jactor.blackjack.compose.model.StartManualGame
import com.github.jactor.blackjack.compose.state.BlackjackState
import com.github.jactor.blackjack.compose.state.Lce

internal val ARRANGE_5_DP_SPACING = Arrangement.spacedBy(5.dp)

@Composable
@Preview
internal fun BlackjackUI(newGameOption: (GameOption) -> Unit = {}, blackjackState: BlackjackState = BlackjackState { PlayerName("jactor") }) {
    var gameState: Lce<GameOfBlackjack> by remember { mutableStateOf(BlackjackState.NotStartet) }
    blackjackState.gameStateConsumer = { newGameState: Lce<GameOfBlackjack> -> gameState = newGameState }

    MaterialTheme {
        Column(modifier = Modifier.fillMaxSize().padding(15.dp), verticalArrangement = ARRANGE_5_DP_SPACING) {
            Row(modifier = Modifier.align(Alignment.CenterHorizontally), horizontalArrangement = ARRANGE_5_DP_SPACING) {
                Text("Hi ${blackjackState.currentPlayerName.invoke().capitalized}! Banken challenge you to a game of Blackjack.")
            }

            Row(modifier = Modifier.align(Alignment.CenterHorizontally), horizontalArrangement = ARRANGE_5_DP_SPACING) {
                Button(
                    onClick = { blackjackState.play() }
                ) {
                    Text("Play automatic game of blackjack")
                }

                Button(
                    onClick = { blackjackState.play(StartManualGame()) }
                ) {
                    Text("Play manual game of blackjack")
                }
            }

            when (gameState) {
                is Lce.Loading -> LoadingUI()
                is Lce.Error -> ErrorUI(failure = gameState as Lce.Error, gameOption = newGameOption)
                is Lce.Content -> GameOfBlackjackUI(
                    gameOfBlackjack = (gameState as Lce.Content<GameOfBlackjack>).data,
                    blackjackState = blackjackState,
                    newGameOption = newGameOption
                )
            }
        } // end column
    } // end material theme
}

@Composable
private fun LoadingUI() {
    Box(modifier = Modifier.fillMaxSize()) {
        CircularProgressIndicator(
            modifier = Modifier
                .align(alignment = Alignment.Center)
                .defaultMinSize(minWidth = 96.dp, minHeight = 96.dp)
        )
    }
}

@Composable
private fun ErrorUI(failure: Lce.Error, gameOption: (GameOption) -> Unit) {
    val cause = failure.error::class.simpleName
    val messages = ExceptionUtil.initErrorMessagesFrom(failure.error)

    MaterialTheme {
        Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            Row(modifier = Modifier.align(Alignment.CenterHorizontally), horizontalArrangement = ARRANGE_5_DP_SPACING) {
                Text(text = "$cause!", textAlign = TextAlign.Left, color = Color.Red)
                Icon(Icons.TwoTone.Warning, "")
            }

            messages.forEach { message ->
                Text(text = message, textAlign = TextAlign.Center, color = Color.Red)
            }

            Row(modifier = Modifier.align(Alignment.CenterHorizontally), horizontalArrangement = ARRANGE_5_DP_SPACING) {
                Button(onClick = { gameOption.invoke(GameOption.QUIT) }) { Text("Exit game!") }
                Button(onClick = { gameOption.invoke(GameOption.PLAYER_NAME) }) { Text("Retry game with new player!") }
            }
        }
    }
}
